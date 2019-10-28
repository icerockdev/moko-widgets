//
//  GridWidgetViewFactory.swift
//  Created by Stas Rybakov on 15/03/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter

public class GridWidgetViewFactory {
  public static func setup() {
    GridListWidgetViewFactoryKt.gridListWidgetViewFactory = { (controller, gridWidget) -> UIView in

      let layout = VerticalTileLayout()
      layout.cellPadding = 16 //TODO: resolve from controller/application/widget style ?
      layout.columnsCount = Int(gridWidget.style.spanCount)
      
      let collectionView = UIFlatUnitCollectionView(frame: controller.view.bounds, collectionViewLayout: layout)
      if let edges = (controller as? AdditionalContainerEdgesProvider)?.additionalEdges(forContainer: collectionView) {
        collectionView.contentInset = edges
      }
      collectionView.backgroundColor = .clear
      collectionView.translatesAutoresizingMaskIntoConstraints = false
      collectionView.setup(layoutStrategy: .selfSizableCells(layout: layout),
                           onRefresh: gridWidget.onRefresh,
                           onReachEnd: gridWidget.onReachEnd)

      gridWidget.items.addObserver(observer: { [weak collectionView] items in
        guard let units = items as? [UIAnyCellUnitProtocol] else { return }
        collectionView?.updateUnits(units,
                                    withReloadStrategy: .diffByHash(hashes: units.map{ $0.stringHash() }))
      })
      return collectionView
    }
  }
}

