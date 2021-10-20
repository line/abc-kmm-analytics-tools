//
//  ATEventTriggerCompatible.swift
//  iosApp
//
//  Created by Steve Kim on 2021/03/15.
//  Copyright © 2021 orgName. All rights reserved.
//

import shared
import UIKit

extension ATEventTriggerCompatible {
    var eventTrigger: ATEventTrigger<Self> {
        ATEventTriggerFactory.Companion().create(owner: self) as! ATEventTrigger<Self>
    }
}
