//
//  ViewController.swift
//  countries
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import UIKit

class ViewController: UITableViewController {
    var continentList = Continents()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // URL for our plist
        if let pathURL = Bundle.main.url(forResource: "continents", withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                continentList.continentData = try plistdecoder.decode([String: [String]].self, from: data)
                continentList.continents = Array(continentList.continentData.keys)
            } catch {
                // handle error
                print(error)
            }
        }
        
        //enables large titles
        navigationController?.navigationBar.prefersLargeTitles = true
    }

    override func viewWillAppear(_ animated: Bool) {
        //print("view will appear")
    }
    
    @IBAction func preparetounwind(_ segue: UIStoryboardSegue){
        //print("unwinding")
    }
    
    //Required methods for UITableViewDataSource
    //Number of rows in the section
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return continentList.continents.count
    }
    
    // Displays table view cells
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //configure the cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "CellIdentifier", for: indexPath)
        cell.textLabel?.text = continentList.continents[indexPath.row]
        return cell
    }
    
    //Handles segues to other view controllers
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "countrysegue" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            //sets the data for the destination controller
            detailVC.title = continentList.continents[indexPath.row]
            detailVC.continentListDetail=continentList
            detailVC.selectedContinent = indexPath.row
        } //for detail disclosure 
        else if segue.identifier == "continentsegue"{
            let infoVC = segue.destination as! ContinentInfoTableViewController
            let editingCell = sender as! UITableViewCell
            let indexPath = tableView.indexPath(for: editingCell)
            infoVC.name = continentList.continents[indexPath!.row]
            let countries = continentList.continentData[infoVC.name]! as [String]
            infoVC.number = String(countries.count)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

