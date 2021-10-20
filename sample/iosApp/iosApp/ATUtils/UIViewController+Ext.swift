//
//  UIViewController+Ext.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/17.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

extension UIViewController {
    static var topMost: UIViewController? {
        UIViewControllerUtil.Companion().topMost()
    }
    
    var className: String {
        let regex = try! NSRegularExpression(pattern: "<.(.*)>", options: .allowCommentsAndWhitespace)
        let str = String(describing: type(of: self))
        let range = NSMakeRange(0, str.count)
        return regex.stringByReplacingMatches(in: str, options: .reportCompletion, range: range, withTemplate: "")
    }
}
