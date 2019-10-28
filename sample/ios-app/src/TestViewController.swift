/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryUnits

class TestViewController: UIViewController {
    
    @IBOutlet private var tableView: UITableView!
    private var dataSource: UITableViewDataSource!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let testing = Testing(unitFactory: self)
        
        let dataSource = FlatUnitTableViewDataSource()
        dataSource.setup(for: tableView)
        
        let units = testing.getUnits() as! [UITableViewCellUnitProtocol]
        dataSource.units = units
        
        tableView.tableFooterView = UIView()
        self.dataSource = dataSource
    }
}

extension TestViewController: TestingUnitFactory {
    func createSimpleUnit(id: Int64, title: String, itemData: ItemData?) -> UnitItem {
        // without R.swift
//        return UITableViewCellUnit<SimpleCell>(
//            data: SimpleCell.CellModel(id: id, title: title),
//            configurator: nil
//        )
        // with R.swift
        return UITableViewCellUnit<SimpleCell>(
            data: SimpleCell.CellModel(id: id, title: title),
            reusable: R.nib.simpleCell,
            configurator: nil
        )
    }
}
