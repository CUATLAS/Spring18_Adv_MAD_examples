//
//  ViewController.swift
//  scrabbleQ
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import UIKit

class ViewController: UITableViewController{
    
    var words = [String]()
    var searchController : UISearchController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // URL for our plist
        if let pathURL = Bundle.main.url(forResource: "qwordswithoutu1", withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                words = try plistdecoder.decode([String].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }

        //search results
        let resultsController = SearchResultsController() //create an instance of our SearchResultsController class
        resultsController.allwords = words //set the allwords property to our words array
        searchController = UISearchController(searchResultsController: resultsController) //initialize our search controller with the resultsController when it has search results to display
        
        //search bar configuration
        searchController.searchBar.placeholder = "Enter a search term" //place holder text
        //searchController.searchBar.sizeToFit() //sets appropriate size for the search bar
        tableView.tableHeaderView=searchController.searchBar //install the search bar as the table header
        searchController.searchResultsUpdater = resultsController //sets the instance to update search results
    }

    //Required methods for UITableViewDataSource
    //Number of rows in the section
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return words.count
    }
    
    // Displays table view cells
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //dequeues an existing cell if one is available, or creates a new one and adds it to the table
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellIdentifier", for: indexPath)
        cell.textLabel?.text = words[indexPath.row]
        return cell
    }

    //UITableViewDelegate method that is called when a row is selected
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let alert = UIAlertController(title: "Row selected", message: "You selected \(words[indexPath.row])", preferredStyle: .alert)
        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(okAction)
        present(alert, animated: true, completion: nil)
        tableView.deselectRow(at: indexPath, animated: true) //deselects the row that had been choosen
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

