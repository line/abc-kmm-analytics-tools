//
//  ControlsViewController.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/17.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

final class ControlsViewController: UIViewController {
    
    deinit {
        print("deinit:\(self)")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        control.eventTrigger
            .click(source: "hello control")
            .register { [EventParam.UserName(value: "steve")] }
        button.eventTrigger
            .click(source: "hello button")
            .register { [EventParam.UserName(value: "steve")] }
    }
    
    @IBOutlet private weak var control: UIControl!
    @IBOutlet private weak var button: UIButton!
}
