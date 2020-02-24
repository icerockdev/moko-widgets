package dev.icerock.moko.widgets.screen

import kotlinx.cinterop.ExportObjCClass
import platform.UIKit.*

@ExportObjCClass
class SearchViewController(
    private val isLightStatusBar: Boolean?
) : UIViewController(nibName = null, bundle = null), UISearchResultsUpdatingProtocol {
    var navItem: UINavigationItem? = null

    override fun viewDidLoad() {
        super.viewDidLoad()
        setupSearchController()
    }

    fun setupSearchController() {
        navItem = UINavigationItem(title = "SearchViewController")
        val searchController = UISearchController(searchResultsController = null)
        // Setup the Search Controller
        searchController.searchResultsUpdater = this
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search"
        navItem!!.searchController = searchController
        definesPresentationContext = true

        val searchField = searchController.searchBar.searchTextField
        searchField.textColor = UIColor.redColor
    }

    override fun updateSearchResultsForSearchController(searchController: UISearchController) {
        print("\n updateSearchResults \n")
    }
}
