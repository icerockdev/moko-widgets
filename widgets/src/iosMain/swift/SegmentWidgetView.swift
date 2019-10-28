//
//  SegmentWidgetView.swift
//  Created by Stas Rybakov on 19/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit

public class SegmentWidgetView: UILoadableView {
  
  @IBOutlet public var segmentControll: UISegmentedControl!
  @IBOutlet public var contentView: UIView!
  
  private var containerView = [Int: UIView]()
  
  override public var nibName: String {
    return "SegmentWidgetView"
  }
  override public var bundle: Bundle { return Bundle(for: self.classForCoder) }
  
  func addInContainer(view: UIView, withIndex index: Int) {
    contentView.addSubview(view)
    view.isHidden = true
    containerView[index] = view
    contentView.setChildFullfill(view)
  }
  
  func showViewWithIndex(selectedIndex: Int) {
    if let selectedView = containerView[selectedIndex] {
      for (_ ,view) in containerView {
        view.isHidden = true
      }
      selectedView.isHidden = false
    }
  }
  
  @IBAction func onChangeSegment(_ sender: UISegmentedControl) {
    showViewWithIndex(selectedIndex: sender.selectedSegmentIndex)
  }
}
