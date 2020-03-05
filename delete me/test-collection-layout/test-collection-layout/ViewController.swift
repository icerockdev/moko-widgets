//
//  ViewController.swift
//  test-collection-layout
//
//  Created by Andrey Tchernov on 04/03/2020.
//  Copyright © 2020 Ice Rock. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
  
  override func viewDidLoad() {
    super.viewDidLoad()
    // Do any additional setup after loading the view.
  }
  var testCell: UICollectionViewCell? = nil
  var testWidthConstraint: NSLayoutConstraint? = nil
}

extension ViewController: UICollectionViewDataSource {
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return 20
  }
  
  func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    return collectionView.dequeueReusableCell(withReuseIdentifier: "testcell", for: indexPath)
  }
}

extension ViewController: UICollectionViewDelegateFlowLayout {
  
  func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
    let fixedWidth: CGFloat = 140

    if (testCell == nil) {
      testCell = collectionView.dequeueReusableCell(withReuseIdentifier: "testcell", for: indexPath)
      testWidthConstraint = testCell?.contentView.widthAnchor.constraint(equalToConstant: fixedWidth)
    }
    guard let cell = testCell else {
      return .zero
    }
    testWidthConstraint?.constant = fixedWidth
    testWidthConstraint?.isActive = true
    let result = CGSize(width: fixedWidth, height: cell.contentView.calculateContentHeight(fromWidth: fixedWidth))
    return result
  }
  
}

extension UIView {
  func calculateContentHeight(fromWidth: CGFloat) -> CGFloat {
    var comprSize = UIView.layoutFittingCompressedSize
    comprSize.width = fromWidth
    updateConstraints()
    layoutSubviews()
    let toSize = systemLayoutSizeFitting(comprSize, withHorizontalFittingPriority: UILayoutPriority.required, verticalFittingPriority: UILayoutPriority.defaultLow)
    return toSize.height
  }
}

class CustomLayout: UICollectionViewFlowLayout {
  
}
