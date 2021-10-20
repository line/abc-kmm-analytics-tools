//
//  UITableViewCell+ATEventTriggerCompatible.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/18.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import RxCocoa
import RxSwift
import UIKit

extension UITableViewCell: ATEventTriggerUICompatible {
    public func registerTrigger(invoke: @escaping () -> Void) {
        rx.methodInvoked(#selector(setSelected(_:animated:)))
            .compactMap { $0.first as? Bool }
            .filter { $0 }
            .bind { _ in invoke() }
            .disposed(by: disposeBag)
    }
}
