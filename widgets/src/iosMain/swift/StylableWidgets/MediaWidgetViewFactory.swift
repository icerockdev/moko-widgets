import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter
import Differ
import RxSwift
import RxCocoa

public class MediaWidgetViewFactory {
    public static func setup() {
        MediaWidgetViewFactoryKt.mediaWidgetViewFactory = { (controller, widget) -> UIView in
            let view = MediaWidgetView(frame: CGRect(x: 0, y: 0, width: 222, height: 222))
            view.bindImage(liveData: widget.field.data)
            view.setup(onTap: {
                widget.onWidgetPressed()
            })
            return view
        }
    }
}

class MediaWidgetView: UILoadableView {
    @IBOutlet var avatarView: UIView!
    @IBOutlet var avatarImageView: UIImageView!
    @IBOutlet var buttonView: UIView!
    @IBOutlet var buttonImageView: UIImageView!
    private var tapGesture = UITapGestureRecognizer()
    private let coverView = UIView(frame: CGRect.zero)
    private let disposeBag = DisposeBag()
    
    func setup(onTap: @escaping () -> ()) {
        self.addSubview(coverView)
        tapGesture = UITapGestureRecognizer()
        coverView.addGestureRecognizer(tapGesture)
        tapGesture.rx.event.bind(onNext: { [weak self] recognizer in
            onTap()
        })
            .disposed(by: disposeBag)
    }
    
    override var nibName: String {
        return "MediaWidgetView"
    }
    
    override var bundle: Bundle { return Bundle(for: self.classForCoder) }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        coverView.frame = self.bounds
        avatarView.maximizeCornerRadius()
        avatarImageView.maximizeCornerRadius()
        buttonView.maximizeCornerRadius()
        buttonImageView.maximizeCornerRadius()
    }
    
    override var intrinsicContentSize: CGSize {
        return self.frame.size
    }
    
    var photo: UIImage? = nil {
        didSet {
            if photo != nil {
                buttonView.isHidden = false
                avatarImageView.image = photo
            }
        }
    }
}

extension MediaWidgetView {
  func bindImage(liveData: LiveData<Bitmap>) {
    setImage(bitmap: liveData.value)
    liveData.addObserver { [weak self] bitmap in
      self?.setImage(bitmap: bitmap)
    }
  }
  
  private func setImage(bitmap: Bitmap?) {
    photo = bitmap?.image
  }
}
