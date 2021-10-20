//
//  UIIntegrationViewController.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/17.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

final class UIIntegrationViewController: UITableViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        clearsSelectionOnViewWillAppear = true
        
        ATEventCenter.Companion().setConfiguration {
            $0.canTrackScreenView { _ in
                true
            }
            $0.canTrackScreenCapture {
                true
            }
            $0.mapScreenClass {
                $0.className
            }
            $0.topViewController {
                UIViewController.topMost
            }
            $0.register(delegate: GADelegate())
        }
    }
}
