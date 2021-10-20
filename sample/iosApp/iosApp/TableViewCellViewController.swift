//
//  TableViewCellViewController.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/17.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

final class TableViewCellViewController: UITableViewController {
    
    deinit {
        print("deinit:\(self)")
    }
}

final class SampleTableViewCell: UITableViewCell {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        eventTrigger
            .click { [weak textLabel] in textLabel?.text ?? "" }
            .register { [EventParam.UserName(value: "steve")] }
    }
}
