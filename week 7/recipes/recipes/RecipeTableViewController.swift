//
//  RecipeTableViewController.swift
//  recipes
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import UIKit
import Firebase

class RecipeTableViewController: UITableViewController {
    
    var ref: DatabaseReference!
    
    var recipes = [Recipe]()
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        ref = Database.database().reference()
        
        //set up a listener for Firebase data change events
        //this event will fire with the initial data and then all data changes
        ref.observe(DataEventType.value, with: {snapshot in
            self.recipes=[]
            //DataSnapshot represents the Firebase data at a given time
            //loop through all the child data nodes
            for recipe in snapshot.children.allObjects as! [DataSnapshot]{
                if let recipeValue = recipe.value as? [String: String], //get value as a Dictionary
                    let json = try? JSONEncoder().encode(recipeValue), //encode as JSON
                    let newRecipe = try? JSONDecoder().decode(Recipe.self, from: json) //decode JSON to Recipe
                {
                    //add recipe to recipes array
                    self.recipes.append(newRecipe)
                }
            }
            self.tableView.reloadData()
        })

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
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
        // #warning Incomplete implementation, return the number of rows
        return recipes.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "recipecell", for: indexPath)
        let recipe = recipes[indexPath.row]
        cell.textLabel!.text = recipe.name
        return cell
    }

    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }


    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let recipe = recipes[indexPath.row]
            //create a child reference in Firebase where the key value is the recipe name
            let reciperef = ref.child(recipe.name)
            // Delete the row from Firebase
            reciperef.ref.removeValue()
        }

//        else if editingStyle == .insert {
//            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
//        }    
    }

    @IBAction func unwindSegue(segue: UIStoryboardSegue){
        if segue.identifier == "savesegue" {
            let source = segue.source as! AddViewController
            if source.addedrecipe.isEmpty == false {
                //create new recipe object
                let newRecipe = Recipe(name: source.addedrecipe, url: source.addedurl)
                //add recipe to recipes array
                recipes.append(newRecipe)
                //create Dictionary
                let newRecipeDict = ["name": source.addedrecipe, "url": source.addedurl]
                //create a child reference in Firebase where the key value is the recipe name
                let reciperef = ref.child(source.addedrecipe)
                //write data to Firebase
                reciperef.setValue(newRecipeDict)
            }
        }
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showdetail" {
            let detailVC = segue.destination as! WebViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            let recipe = recipes[indexPath.row]
            //sets the data for the destination controller
            detailVC.title = recipe.name
            detailVC.webpage = recipe.url
        }
    }
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
