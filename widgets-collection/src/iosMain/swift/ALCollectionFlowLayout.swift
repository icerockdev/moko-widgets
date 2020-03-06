/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit

@objc public class ALCollectionFlowLayout: UICollectionViewFlowLayout {
  func widestCellWidth(bounds: CGRect) -> CGFloat {
    guard let collectionView = collectionView else {
      return 0
    }
    
    let insets = collectionView.contentInset
    let width = bounds.width - insets.left - insets.right
    
    if width < 0 { return 0 }
    else { return width }
  }
  
  func updateEstimatedItemSize(bounds: CGRect, insets: UIEdgeInsets) {
    let width: CGFloat
    let height: CGFloat
    
    if scrollDirection == .horizontal {
      width = 200
      height = bounds.height - insets.top - insets.bottom
    } else {
      width = bounds.width - insets.left - insets.right
      height = 200
    }
    
    estimatedItemSize = CGSize(width: width, height: height)
  }
  
  override public func prepare() {
    super.prepare()
    
    guard let cv = collectionView else { return }
    
    updateEstimatedItemSize(bounds: cv.bounds, insets: cv.adjustedContentInset)
  }
  
  override public func shouldInvalidateLayout(forBoundsChange newBounds: CGRect) -> Bool {
    guard let cv = collectionView else { return false }
    
    let oldSize = cv.bounds.size
    guard oldSize != newBounds.size else { return false }
    
    updateEstimatedItemSize(bounds: newBounds, insets: cv.adjustedContentInset)
    return true
  }
}
