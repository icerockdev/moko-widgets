/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.units.TableUnitItem
import dev.icerock.moko.units.UnitTableViewDataSource
import dev.icerock.moko.widgets.utils.bind
import dev.icerock.moko.widgets.utils.getStatusBarStyle
import dev.icerock.moko.widgets.utils.setEventHandler
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.UISearchController
import platform.UIKit.UISearchResultsUpdatingProtocol
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UITableView
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.bottomAnchor
import platform.UIKit.leadingAnchor
import platform.UIKit.navigationItem
import platform.UIKit.searchTextField
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints

class SearchViewController(
    private val isLightStatusBar: Boolean?,
    private val searchQuery: MutableLiveData<String>,
    private val searchItems: LiveData<List<TableUnitItem>>,
    private val searchPlaceholder: StringDesc
) : UIViewController(nibName = null, bundle = null), UISearchResultsUpdatingProtocol {

    private lateinit var unitsDataSource: UnitTableViewDataSource

    override fun viewDidLoad() {
        super.viewDidLoad()

        setupSearchController()
        setupTableView()
    }

    private fun setupSearchController() {
        val searchController = UISearchController(searchResultsController = null)
        // Setup the Search Controller
        searchController.searchResultsUpdater = this
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = searchPlaceholder.localized()

        navigationItem.searchController = searchController

        definesPresentationContext = true
    }

    private fun setupTableView() {
        val tableView = UITableView(frame = CGRectZero.readValue()).apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        unitsDataSource = UnitTableViewDataSource(tableView = tableView)

        searchItems.bind { unitsDataSource.unitItems = it }

        with(view) {
            addSubview(tableView)

            leadingAnchor.constraintEqualToAnchor(tableView.leadingAnchor).active = true
            tableView.trailingAnchor.constraintEqualToAnchor(trailingAnchor).active = true
            topAnchor.constraintEqualToAnchor(tableView.topAnchor).active = true
            tableView.bottomAnchor.constraintEqualToAnchor(bottomAnchor).active = true
        }
    }

    override fun updateSearchResultsForSearchController(searchController: UISearchController) {
        searchQuery.value = searchController.searchBar.text.orEmpty()
    }

    override fun preferredStatusBarStyle(): UIStatusBarStyle {
        val isLight = isLightStatusBar ?: application.isLightStatusBar
        return getStatusBarStyle(isLight) ?: super.preferredStatusBarStyle()
    }
}
