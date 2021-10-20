//
//  UIControl+ATEventTriggerUICompatible.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/15.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

extension UIControl: ATEventTriggerUICompatible {
    public func registerTrigger(invoke: @escaping () -> Void) {
        rx.controlEvent(.touchUpInside)
            .bind { invoke() }
            .disposed(by: disposeBag)
    }
}
