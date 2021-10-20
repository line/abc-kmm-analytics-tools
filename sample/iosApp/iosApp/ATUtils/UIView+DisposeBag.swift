//
//  UIView+Ext.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/18.
//  Copyright © 2021 orgName. All rights reserved.
//

import RxSwift
import UIKit

extension UIView {
    public var disposeBag: DisposeBag {
        get {
            guard let value = objc_getAssociatedObject(self, &disposeBagKey) as? DisposeBag else {
                let value = DisposeBag()
                objc_setAssociatedObject(self, &disposeBagKey, value, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
                return value
            }
            return value
        }
        set {
            objc_setAssociatedObject(self, &disposeBagKey, newValue, objc_AssociationPolicy.OBJC_ASSOCIATION_RETAIN_NONATOMIC)
        }
    }
}

private var disposeBagKey: UInt8 = 0
