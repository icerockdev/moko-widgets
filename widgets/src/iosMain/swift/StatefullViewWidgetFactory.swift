//
//  StatefullViewWidgetFactory.swift
//  Created by Andrey Tchernov on 13/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import MultiPlatformLibrary

public class StatefullViewWidgetFactory {
  public static func setup() {
    // TODO такое вот можно легко в котлине реализовать, чтобы не требовался свифт файл дополнительный и без вызова setup обойтись можно будет
    StatefulWidgetViewFactoryKt.statefulWidgetViewFactory = { (controller, stateWidget) -> UIView in
      let errorView = stateWidget.errorWidget.buildView(viewFactoryContext: controller)
      let dataView = stateWidget.dataWidget.buildView(viewFactoryContext: controller)
      let emptyView = stateWidget.emptyWidget.buildView(viewFactoryContext: controller)
      let loadingView = stateWidget.loadingWidget.buildView(viewFactoryContext: controller)
      let rootView = UIView(frame: .zero)
      rootView.translatesAutoresizingMaskIntoConstraints = false
      rootView.addSubview(dataView)
      rootView.addSubview(emptyView)
      rootView.addSubview(errorView)
      rootView.addSubview(loadingView)

      [dataView, emptyView, errorView, loadingView].forEach { view in
        rootView.setChildFullfill(view)
      }

      stateWidget.stateLiveData.addObserver(observer: { (state: State<AnyObject, AnyObject>?) in
        guard let state = state else { return }
        
        dataView.isHidden = !(state.isSuccess())
        emptyView.isHidden = !(state.isEmpty())
        loadingView.isHidden = !(state.isLoading())
        errorView.isHidden = !(state.isError())
      })

      return rootView
    }
  }
}
