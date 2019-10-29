/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryCore
import MultiPlatformLibraryBinderAdapter
import Differ

public class LinearWidgetViewFactory {
  public static func setup() {
    LinearListWidgetViewFactoryKt.linearListWidgetViewFactory = { (controller, widget) -> UIView in
      let tableView = UIFlatUnitTableView(frame: controller.view.bounds)
      tableView.translatesAutoresizingMaskIntoConstraints = false
      tableView.alwaysBounceVertical = false
      tableView.alwaysBounceHorizontal = false
      tableView.separatorStyle = .none
      tableView.backgroundColor = .clear
      if(widget.style.reversed) {
        tableView.transform = CGAffineTransform(rotationAngle: -(CGFloat)(Double.pi))
        tableView.scrollIndicatorInsets = UIEdgeInsets(top: 0.0, left: 0.0, bottom: 0.0, right: tableView.bounds.size.width - 8.0)
        tableView.reversed = true
      }
      tableView.setup()
      tableView.flatUnitSource.onReachEnd = {
        let _ = widget.onReachEnd?()
      }
      widget.source.liveData().addObserver(observer: { [weak tableView] items in
        guard let units = items as? [UITableViewCellUnitProtocol] else { return }
        
        tableView?.reloadUnits(units)
      })
      
      if let nAction = widget.onRefresh {
        tableView.setRefresh({
          let _ = nAction()
        })
      }
      
      return tableView
    }
  }
}

//TODO: Split by files in scl/mpp, scl/ios
class UIFlatUnitTableView: UITableView {
  let flatUnitSource = ReachEndUnitDataSource()
  var reversed: Bool = false
  var registeredCellsPool: RegisteredCellsPool?
  private var refreshAction: (() -> Void)?
  
  func setup() {
    flatUnitSource.reversed = reversed
    flatUnitSource.setup(for: self)
    registeredCellsPool = RegisteredCellsPool(tableView: self)
  }
  
  func setRefresh(_ withAction: (() -> Void)?) {
    let refresh = UIRefreshControl()
    refreshAction = withAction
    refresh.addTarget(self, action: #selector(onRefreshValueChanged(_:)), for: .valueChanged)
    self.refreshControl = refresh
  }
  
  @objc func onRefreshValueChanged(_ sneder: AnyObject) {
    self.refreshAction?()
  }
  
  func reloadUnits(_ units: [UITableViewCellUnitProtocol]) {
    registeredCellsPool?.update(withUnits: units)
    flatUnitSource.reloadUnits(units, in: self)
  }
}

class ReachEndUnitDataSource: FlatUnitTableViewDataSource {
  var onReachEnd: (() -> Void)? = nil
  var reversed: Bool = false

  private weak var tableView: UITableView?
  private var needAnimateToUnits: [UITableViewCellUnitProtocol]? = nil
  private var isAnimated = false
  private var estimatedHeights = [IndexPath: CGFloat]()
  
  override func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
    return estimatedHeights[indexPath] ?? super.tableView(tableView, estimatedHeightForRowAt: indexPath)
  }
  
  func tableView(_ tableView: UITableView, didEndDisplaying cell: UITableViewCell, forRowAt indexPath: IndexPath) {
      estimatedHeights[indexPath] = cell.frame.height
  }
  
  override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    let rowCount = self.tableView(tableView, numberOfRowsInSection: indexPath.section)
    if indexPath.row == rowCount - 1 {
      onReachEnd?()
    }
    
    let cell = super.tableView(tableView, cellForRowAt: indexPath)

    if(reversed) {
      cell.transform = CGAffineTransform(rotationAngle: CGFloat(Double.pi))
    }
    
    return cell
  }
  
  private func checkNextAnimation() {
    objc_sync_enter(self)
    guard let newUnits = needAnimateToUnits.map({ $0 }) else {
      self.isAnimated = false
      tableView?.refreshControl?.endRefreshing()
      objc_sync_exit(self)
      return
    }
    needAnimateToUnits = nil
    objc_sync_exit(self)
    animateChanges(toUnits: newUnits, forced: true)
  }
  
  private func animateChanges(toUnits newUnits: [UITableViewCellUnitProtocol], forced: Bool = false) {
    self.isAnimated = true
    let hashes = units.map({ "\($0.reuseId)_\(($0.itemData() as? StringHashable)?.stringHash() ?? "")" })
    let newHashes = newUnits.map({ "\($0.reuseId)_\(($0.itemData() as? StringHashable)?.stringHash() ?? "")" })
    self.units = newUnits
    tableView?.refreshControl?.endRefreshing()
    CATransaction.begin()
    CATransaction.setAnimationDuration(CFTimeInterval(0.25))
    CATransaction.setCompletionBlock {
      [weak self] in
      guard let tableView = self?.tableView else { return }
      let visibleIndexes = (tableView.indexPathsForVisibleRows) ?? []
      visibleIndexes.forEach({
        self?.unit(from: $0)?.update(cell: tableView.cellForRow(at: $0) as Any)
      })
      self?.checkNextAnimation()
    }
    tableView?.animateRowChanges(
      oldData: hashes,
      newData: newHashes)
    CATransaction.commit()
  }
  
  func reloadUnits(_ newUnits: [UITableViewCellUnitProtocol], in tableView: UITableView) {
    self.tableView = tableView
    if (isAnimated) {
      needAnimateToUnits = newUnits
      //TODO: Убрать обновление пачками элементов
    }
    animateChanges(toUnits: newUnits)
  }
}
