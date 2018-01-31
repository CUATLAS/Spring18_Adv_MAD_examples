//
//  SearchResultsController.swift
//  scrabbleQ
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import UIKit

class SearchResultsController: UITableViewController, UISearchResultsUpdating {
    
    var allwords = [String]()
    var filteredWords = [String]()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        //register our table cell identifier
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "CellIdentifier")

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    //UISearchResultsUpdating protocol required method to implement the search
    func updateSearchResults(for searchController: UISearchController) {
        let searchString = searchController.searchBar.text //search string
        filteredWords.removeAll(keepingCapacity: true) //removes all elements
        if searchString?.isEmpty == false {
            //closure that will be called for each word to see if it matches the search string
            let searchfilter: (String) -> Bool = { name in
                //look for the search string as a substring of the word
                let range = name.range(of: searchString!, options: .caseInsensitive)
                return range != nil //returns true if the value matches and false if there’s no match
            } //end closure
            let matches = allwords.filter(searchfilter)
            filteredWords.append(contentsOf: matches)
        }
        tableView.reloadData() //reload table data with search results
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredWords.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellIdentifier", for: indexPath)
        cell.textLabel?.text = filteredWords[indexPath.row]
        return cell
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
