//
//  SegmentWidgetViewFactory.swift
//  Created by Stas Rybakov on 18/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import MultiPlatformLibrary

public class SegmentWidgetViewFactory {
  
  public static func setup() {
    TabsWidgetViewFactoryKt.tabsWidgetViewFactory = { (controller, segmentWidget) -> UIView in
      // TODO вью от проекта должна быть предоставлена или вообще лежать тут в ресурсах
      let segmentView = SegmentWidgetView(frame: CGRect.zero)
      let segmentControl = segmentView.segmentControll!
//      segmentView.translatesAutoresizingMaskIntoConstraints = false //Hack for politsturtup only, remove in scl
      segmentControl.removeAllSegments()
      //segmentControl.apportionsSegmentWidthsByContent = !(UIScreen.main.bounds.width > 320)
      for (index, segment) in segmentWidget.tabs.enumerated() {
        let titleLiveData = segment.title.liveData()
        let currentTitleValue = titleLiveData.value?.localized() ?? ""
        segmentControl.insertSegment(withTitle: currentTitleValue, at: index, animated: false)
        
        titleLiveData.addObserver(observer: { [weak segmentControl] title in
          segmentControl?.setTitle(title?.localized() ?? "", forSegmentAt: index)
        })
        
        let view = segment.body.buildView(viewFactoryContext: controller)
        segmentView.addInContainer(view: view, withIndex: index)
      }
      segmentControl.selectedSegmentIndex = 0
      segmentView.showViewWithIndex(selectedIndex: 0)

      return segmentView
    }
  }
}
