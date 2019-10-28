//
//  MediaCollectionWidget.swift
//  GetChallenge
//
//  Created by Stanislav Rachenko on 24/07/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import Foundation
import MultiPlatformLibrary
import MultiPlatformLibraryCore
import MultiPlatformLibraryBinderAdapter
import MultiPlatformLibraryMvvm

public class MediaCollectionWidgetView {
    public static func setup() {
        MediaCollectionWidgetViewFactoryKt.mediaCollectionWidgetViewFactory = { (controller, widget) -> UIView in
            let view = CollectionWidgetView(widget: widget)
            return view
        }
    }
}

class CollectionWidgetView: UIView, UICollectionViewDelegate {
    var widget: MediaCollectionWidget!
    var collectionView: UICollectionView!
    var dataSource: FlatUnitCollectionViewDataSource?
    var itemSize: CGFloat = 140
    var formInsets: CGFloat = 32
    
    init(widget: MediaCollectionWidget) {
        self.widget = widget
        let layout = UICollectionViewFlowLayout()
        
        layout.itemSize = CGSize(width: itemSize, height: itemSize)
        layout.minimumLineSpacing = 16
        layout.minimumInteritemSpacing = 16
        layout.sectionInset = .init(top: 8, left: 0, bottom: 8, right: 0)
        layout.scrollDirection = .horizontal
        self.collectionView = UICollectionView(frame: CGRect.zero, collectionViewLayout: layout)
        self.collectionView.backgroundColor = .clear
        self.collectionView.showsHorizontalScrollIndicator = false
        super.init(frame: CGRect.zero)
        setupView()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    func setupView() {
        let data = widget.field.data.value
        dataSource = FlatUnitCollectionViewDataSource()
        self.addCreateUnit()
        dataSource?.setup(for: collectionView)
        widget.field.data.addObserver(observer: { (data) in
            if let bitmapArray = data as? Array<Media> {
                self.dataSource?.units = []
                for mediaField in bitmapArray {
                    let dataCell = ChallengeMediaCell.CellModel(image: mediaField.preview.image,
                                                                isPhoto: mediaField.type == .photo,
                                                                isDeleteHidden: mediaField.isServer(),
                                                                onTap: {
                        self.widget.itemClickListener(mediaField)
                    }, onDelete: {
                        self.widget.deleteListener(mediaField)
                    })
                    let unit = UIBindingAnyCellUnit<ChallengeMediaCell>(data: dataCell, reuseId: ChallengeMediaCell.reuseId, nibName: ChallengeMediaCell.reuseId, configurator: nil)
                    self.dataSource?.units.append(unit)
                }
                self.addCreateUnit()
                self.collectionView.reloadData()
            }
        })
        let view = UIView(frame: CGRect.zero)
        self.translatesAutoresizingMaskIntoConstraints = false
        self.fixSize(height: itemSize + 20)
        self.fillWith(view: collectionView, insets: UIEdgeInsets(horizontal: -formInsets, vertical: 0))
        
        collectionView.register(UINib(nibName: ChallengeMediaCell.reuseId, bundle: Bundle(for: self.classForCoder)), forCellWithReuseIdentifier: ChallengeMediaCell.reuseId)
        collectionView.register(UINib(nibName: CreateChallengeMediaCell.reuseId, bundle: Bundle(for: self.classForCoder)), forCellWithReuseIdentifier: CreateChallengeMediaCell.reuseId)
    }
    
    private func addCreateUnit() {
        let dataCell = CreateChallengeMediaCell.CellModel(onTap: {
            self.widget.addListener()
        })
        let createUnit = UIBindingAnyCellUnit<CreateChallengeMediaCell>(data: dataCell, reuseId: CreateChallengeMediaCell.reuseId, nibName: CreateChallengeMediaCell.reuseId, configurator: nil)
        dataSource?.units.append(createUnit)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        collectionView.reloadData()
        let leftInset = (self.frame.width + formInsets - itemSize) / 2
        let rightInset = leftInset
        collectionView.contentInset = UIEdgeInsets(top: 0, left: leftInset, bottom: 0, right: rightInset)
    }
}
