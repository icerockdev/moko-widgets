/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.units.UnitItem
import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.bind
import dev.icerock.moko.widgets.uikit.WidgetTableViewCell
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSIndexPath
import platform.UIKit.UILabel
import platform.UIKit.UINib
import platform.UIKit.UITableView
import platform.UIKit.UITableViewAutomaticDimension
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewDataSourceProtocol
import platform.UIKit.UITableViewStyle
import platform.UIKit.row
import platform.UIKit.text
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.darwin.NSInteger
import platform.darwin.NSObject

actual var listWidgetViewFactory: VFC<ListWidget> = { viewController, widget ->
    // TODO add styles support
    val style = widget.style

    val unitDataSource = UnitTableViewDataSource()
    val tableView = UITableView(
        frame = CGRectZero.readValue(),
        style = UITableViewStyle.UITableViewStylePlain
    ).apply {
        translatesAutoresizingMaskIntoConstraints = false
        dataSource = unitDataSource
        rowHeight = UITableViewAutomaticDimension
        estimatedRowHeight = UITableViewAutomaticDimension
    }
    val unitsRegistry = UnitsRegistry(tableView)

    widget.items.bind { units ->
        unitsRegistry.registerIfNeeded(units)
        unitDataSource.unitItems = units
        tableView.reloadData()
    }

    WidgetTableViewCell.setViewFactory(factory = {
        UILabel(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
            text = "hello world"
        }
    }, toReuseIdentifier = "testCell")

    tableView
}

//@ExportObjCClass
//class MyTableViewCell : UITableViewCell {
//    val myLabel: UILabel by lazy {
//        UILabel(frame = CGRectZero.readValue()).apply {
//            translatesAutoresizingMaskIntoConstraints = false
//            textColor = UIColor.redColor
//            font = UIFont.boldSystemFontOfSize(24.0)
//            textAlignment = NSTextAlignmentCenter
//        }
//    }
//
//    @OverrideInit
//    constructor(
//        style: UITableViewCellStyle,
//        reuseIdentifier: String?
//    ) : super(style = style, reuseIdentifier = reuseIdentifier)
//
//    @OverrideInit
//    constructor(coder: NSCoder) : super(coder = coder)
//}
//    val myLabel: UILabel by lazy {
//        UILabel(frame = CGRectZero.readValue()).apply {
//            translatesAutoresizingMaskIntoConstraints = false
//            textColor = UIColor.redColor
//            font = UIFont.boldSystemFontOfSize(24.0)
//            textAlignment = NSTextAlignmentCenter
//        }
//    }

//
//    @OverrideInit
//    constructor(
//        style: UITableViewCellStyle,
//        reuseIdentifier: String?
//    ) : super(style, reuseIdentifier) {
//        addSubview(myLabel)
//
//        myLabel.topAnchor.constraintEqualToAnchor(topAnchor).active = true
//        myLabel.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
//        myLabel.leadingAnchor.constraintEqualToAnchor(leadingAnchor).active = true
//        myLabel.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
//    }
//

//    @OverrideInit
//    constructor(frame: CValue<CGRect>) : super(frame = frame)

//    @OverrideInit
//    constructor() : super()
//}

val UnitItem.reuseData: ReuseData
    get() {
        return ReuseData.Class(
            objCClass = WidgetTableViewCell().`class`()!!,
//            objCClass = MyTableViewCell(
//                UITableViewCellStyle.UITableViewCellStyleDefault,
//                ""
//            ).`class`()!!,
            reusableIdentifier = "testCell"
        )
    }

fun UnitItem.bind(cell: UITableViewCell) {
//    if (cell is MyTableViewCell) {
//        cell.myLabel.text = this.toString()
//    } else {
    cell.text = this.toString()
//    }
}

sealed class ReuseData(
    val reusableIdentifier: String
) {
    class Class(private val objCClass: ObjCClass, reusableIdentifier: String) : ReuseData(reusableIdentifier) {
        override fun register(tableView: UITableView) {
            tableView.registerClass(cellClass = objCClass, forCellReuseIdentifier = reusableIdentifier)
        }
    }

    class Nib(private val nib: UINib, reusableIdentifier: String) : ReuseData(reusableIdentifier) {
        override fun register(tableView: UITableView) {
            tableView.registerNib(nib = nib, forCellReuseIdentifier = reusableIdentifier)
        }
    }

    abstract fun register(tableView: UITableView)
}

class UnitsRegistry(private val tableView: UITableView) {
    private val registeredUnits = mutableSetOf<UnitItem>()

    fun registerIfNeeded(unitItem: UnitItem) {
        if (registeredUnits.contains(unitItem)) return

        registeredUnits.add(unitItem)
        unitItem.reuseData.register(tableView)
    }

    fun registerIfNeeded(list: List<UnitItem>) {
        list.forEach { registerIfNeeded(it) }
    }
}

@ExportObjCClass
class UnitTableViewDataSource : NSObject(), UITableViewDataSourceProtocol {

    var unitItems: List<UnitItem>? = null

    override fun tableView(tableView: UITableView, cellForRowAtIndexPath: NSIndexPath): UITableViewCell {
        val position = cellForRowAtIndexPath.row.toInt()
        val unitItem = unitItems?.get(position)
        requireNotNull(unitItem) { "cell can be requested only when unit exist" }

        val cell = tableView.dequeueReusableCellWithIdentifier(
            identifier = unitItem.reuseData.reusableIdentifier,
            forIndexPath = cellForRowAtIndexPath
        )
        unitItem.bind(cell)
        return cell
    }

    override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger {
        return unitItems?.size?.toLong() ?: 0
    }

    override fun numberOfSectionsInTableView(tableView: UITableView): NSInteger {
        return 1
    }
}
