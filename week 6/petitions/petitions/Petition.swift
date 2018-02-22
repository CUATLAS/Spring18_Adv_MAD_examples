//
//  petition.swift
//  petitions
//
//  Created by Aileen Pierce
//  Copyright Aileen Pierce. All rights reserved.
//

import Foundation

struct Petition: Decodable{
    let title: String
    let sigCount : Int
    let url : String
}

