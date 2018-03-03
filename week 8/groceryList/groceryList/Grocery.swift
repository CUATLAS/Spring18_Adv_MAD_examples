//
//  Grocery.swift
//  groceryList
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation
import RealmSwift

class Grocery: Object {
    @objc dynamic var name = ""
    @objc dynamic var bought = false
}

