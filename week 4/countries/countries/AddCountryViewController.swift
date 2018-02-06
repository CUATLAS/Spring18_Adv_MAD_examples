//
//  AddCountryViewController.swift
//  countries
//
//  Created by Aileen Pierce
//  Copyright © 2018 Aileen Pierce. All rights reserved.
//

import UIKit

class AddCountryViewController: UIViewController {
    @IBOutlet weak var countryTextfield: UITextField!
    var addedCountry = String()
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "doneSegue"{
            //only add a country if there is text in the textfield
            if ((countryTextfield.text?.isEmpty) == false){
                addedCountry=countryTextfield.text!
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
