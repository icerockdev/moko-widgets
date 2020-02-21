/*
* Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
*/

import UIKit

@objc public class SearchViewController: UIViewController {
    @IBOutlet weak var navItem: UINavigationItem!
    
    @objc public static func create() -> SearchViewController {
      return SearchViewController(
        nibName: nil,
        bundle: Bundle(for: SearchViewController.self)
      )
    }
    
    func setupSearchController() {
        let searchController = UISearchController(searchResultsController: nil)
        // Setup the Search Controller
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search"
        navItem.searchController = searchController
        definesPresentationContext = true
        
        let searchField = searchController.searchBar.searchTextField
        searchField.textColor = UIColor.red
        
        //tableView.tableHeaderView = searchController.searchBar
    }
}

// MARK: - UISearchResultsUpdating Delegate
extension SearchViewController: UISearchResultsUpdating {
    
    public func updateSearchResults(for searchController: UISearchController) {
        print("\n updateSearchResults \n")
    }
}

