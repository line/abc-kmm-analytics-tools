![abc-kmm-analytics-tools: Analytics Tools for Kotlin Multiplatform Mobile iOS and android](images/cover.png)

[![Kotlin](https://img.shields.io/badge/kotlin-1.5.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![KMM](https://img.shields.io/badge/KMM-0.2.7-lightgreen.svg?logo=KMM)](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
[![AGP](https://img.shields.io/badge/AGP-7.0.1-green.svg?logo=AGP)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-blue.svg?logo=Gradle)](https://gradle.org)
[![Platform](https://img.shields.io/badge/platform-ios,android-lightgray.svg?style=flat)](https://img.shields.io/badge/platform-ios-lightgray.svg?style=flat)

## Index
- [Features](#Features)
- [Example](#Example)
- [Introduce Architecture](#Introduce-Architecture)
- [Installation](#Installation)
- [Configure](#Configure)
  - [Screen Mapper](#Screen-Mapper)
  - [Initialization](#Initialization)
- [Implementation](#Implementation)
  - [Delegate](#Delegate)
  - [Parameters](#Parameters)
  - [ATEventParamProvider](#ATEventParamProvider)
  - [ATEventTriggerUICompatible](#ATEventTriggerUICompatible)
- [Integration with UI](#Integration-with-UI)
- [Advanced](#Advanced)
    - [Dynamic Screen Name Mapping](#Dynamic-Screen-Name-Mapping)
- [TODO](#TODO)

## Features
- Unified handling of various event trackers is possible
- Automatically tracking and sending events of screen view and capture
- Provide screen name mapper
- Remove boilerplate by binding UI elements to event triggers
- Common interfaces available in KMM Shared

## Example
![](/images/sh03.gif)

## Requirements
- iOS
  - Deployment Target 10.0 or higher
- Android
  - minSdkVersion 23

## Installation

### Gradle Settings
Add below gradle settings into your KMP (Kotlin Multiplatform Project)

#### build.gradle.kts in shared
```kotlin
plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

val analyticsTools = "com.linecorp.abc:kmm-analytics-tools:1.1.0"

kotlin {
    android()
    ios {
        binaries
            .filterIsInstance<Framework>()
            .forEach {
                it.baseName = "shared"
                it.transitiveExport = true
                it.export(analyticsTools)
            }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(analyticsTools)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(analyticsTools)
                api(analyticsTools)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("androidx.test:core:1.0.0")
                implementation("androidx.test:runner:1.1.0")
                implementation("org.robolectric:robolectric:4.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(analyticsTools)
                api(analyticsTools)
            }
        }
        val iosTest by getting
    }
}
```
### Project Settings
Android
- You can use right now without other settings.

iOS
1. Create `Podfile` with below setting in your project root.
    ```bash
    use_frameworks!

    platform :ios, '10.0'

    install! 'cocoapods', :deterministic_uuids => false

    target 'iosApp' do
        pod 'shared', :path => '../shared/'
    end
    ```
2. Run command `pod install` on the terminal

## Configure

### Screen Mapper
Screen mapper is mapping system that to map between screen class and screen name. The file `ATScreenNameMapper.json` that defines screen mapping table will automatically used if needed in system.

Android
1. Create a file named `ATScreenNameMapper.json` in your assets folder like below.
![](/images/sh01.png)

2. Write table as json format for screen name mapping.
```json
{
  "MainActivity": "main"
}
```

iOS
1. Create a file named `ATScreenNameMapper.json` in your project like below.
![](/images/sh02.png)

2. Write table as json format for screen name mapping.
```json
{
  "MainViewController": "main"
}
```

### Initialization

Android
```kotlin
ATEventCenter.setConfiguration {
    canTrackScreenView { true }
    canTrackScreenCapture { true }
    register(TSDelegate())
    register(GoogleAnalyticsDelegate())
}
```

iOS
```swift
ATEventCenter.Companion().setConfiguration {
    $0.canTrackScreenView { _ in
        true
    }
    $0.canTrackScreenCapture {
        true
    }
    $0.mapScreenClass {
        $0.className()
    }
    $0.topViewController {
        UIViewController.topMost
    }
    $0.register(delegate: TSDelegate())
    $0.register(delegate: GoogleAnalyticsDelegate())
}

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
```

### Setup User Properties
This function must be called in the scope where the user properties are changed

Android or Shared
```kotlin
ATEventCenter.setUserProperties()
```

iOS
```swift
ATEventCenter.Companion().setUserProperties()
```

## Implementation
### Delegate

Delegate class is proxy for various event trackers.

Android or Shared
```kotlin
class GoogleAnalyticsDelegate: ATEventDelegate {
    // @optional to map key of event
    override fun mapEventKey(event: Event): String {
        event.value
    }

    // @optional to map key of parameter
    override fun mapParamKey(container: KeyValueContainer): String {
        container.key
    }

    // @required to set up event tracker
    override fun setup() {
    }

    // @required to set user properties for event tracker
    override fun setUserProperties() {
    }

    // @required to send event to the event tracker
    override fun send(event: String, params: Map<String, String>) {
    }
}
```

iOS
```swift
final class GoogleAnalyticsDelegate: ATEventDelegate {
    // @required to map key of parameter
    func mapEventKey(event: Event) -> String {
        event.value
    }

    // @required to map key of parameter
    func mapParamKey(container: KeyValueContainer) -> String {
        container.key
    }
    
    // @required to set up event tracker
    func setup() {
    }

    // @required to set user properties for event tracker
    func setUserProperties() {
    }
    
    // @required to send event to the event tracker
    func send(event: String, params: [String: String]) {
    }
}
```

### Parameters

Kotlin
```kotlin
sealed class Param: KeyValueContainer {
    data class UserName(override val value: String): Param()
    data class ViewTime(override val value: Int): Param()
}
```

Swift
```swift
enum Param {
    final class UserName: AnyKeyValueContainer<NSString> {}
    final class ViewTime: AnyKeyValueContainer<KotlinInt> {}
}
```

If you want something more swift, use your own KeyValueContainer.

```swift
enum Param {
    final class UserName: MyKeyValueContainer<String> {}
    final class ViewTime: MyKeyValueContainer<Int> {}
}

class MyKeyValueContainer<T: Any>: KeyValueContainer {
    init(_ value: T) {
        self.value = value
    }
    
    let value: Any
    
    var key: String {
        "\(self)".components(separatedBy: ".").last?.snakenized ?? ""
    }
}
```

### ATEventParamProvider
ATEventParamProvider is used to get extra parameters when send to events and functions are called automatically.

Android
```kotlin
class MainActivity : AppCompatActivity(), ATEventParamProvider {
    override fun params(event: Event, source: Any?): List<KeyValueContainer> {
        return when(event) {
            Event.VIEW -> listOf(
                Param.UserName("steve"),
                Param.ViewTime(10000))
            else -> listOf()
        }
    }
}
```
iOS
```kotlin
extension MainViewController: ATEventParamProvider {
    func params(event: Event, source: Any?) -> [KeyValueContainer] {
        switch event {
        case .view:
            return [
                Param.UserName("steve"), 
                Param.ViewTime(10000)
            ]
        default:
            return []
        }
    }
}
```

### ATEventTriggerUICompatible (iOS Only)

This is an interface to make it easy to connect the click event of a ui element to an event trigger.

```swift
extension UIControl: ATEventTriggerUICompatible {
    public func registerTrigger(invoke: @escaping () -> Void) {
        rx.controlEvent(.touchUpInside)
            .bind { invoke() }
            .disposed(by: disposeBag)
    }
}
```

## Integration with UI

### Android

Legacy

* Using with Event Trigger
```kotlin

sealed class Param: KeyValueContainer {
    data class UserName(override val value: String): Param()
}

class MainActivity : AppCompatActivity(), ATEventParamProvider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        
        button.eventTrigger
            .click("button")
            .register { listOf(Param.UserName("steve")) }

        button.setOnClickListener { v ->
            v.eventTrigger.send()
        }
    }
}
```

* Using Directly
```xml
<Button
      android:id="@+id/button"
      android:text="Hello!"
      android:onClick="onClickHello"/>
```
```kotlin
fun onClickHello(view: View) {
    ATEventCenter.send(
        Event.CLICK,
        listOf(BaseParam.ClickSource("hello")))
}
```

### iOS
#### SwiftUI
```swift
Button("Hello") {
    ATEventCenter.Companion().send(
        event: .click,
        params: [BaseParam.ClickSource(value: "hello")])
}
```
#### Legacy
Using with Event Trigger

* Extension to get instance of ATEventTrigger
```swift
extension ATEventTriggerCompatible {
    var eventTrigger: ATEventTrigger<Self> {
        ATEventTriggerFactory.Companion().create(owner: self) as! ATEventTrigger<Self>
    }
}
```
* Extension to make it easy to connect the click event of a ui element to an event trigger
```swift
extension UIControl: ATEventTriggerUICompatible {
    public func registerTrigger(invoke: @escaping () -> Void) {
        rx.controlEvent(.touchUpInside)
            .bind { invoke() }
            .disposed(by: disposeBag)
    }
}
```

* Client side
```swift
enum Param {
    final class UserName: AnyKeyValueContainer<NSString> { }
}

override func viewDidLoad() {
    super.viewDidLoad()
    
    button.eventTrigger
        .click(source: "hello")
        .register { [Param.UserName("steve")] }
}
```

Using Directly
```swift
@IBAction func clicked(_ sender: UIButton) {
    ATEventCenter.Companion().send(
        event: .click,
        params: [BaseParam.ClickSource(value: "hello")])
}
```

## Advanced

### Dynamic Screen Name Mapping
You can dynamically determine the screen name by implementing ATDynamicScreenNameMappable.

Android
```kotlin
class MainActivity : AppCompatActivity(), ATDynamicScreenNameMappable {
    
    override fun mapScreenName(): String? {
        return "ScreenNameAsYouWant"
    }
}
```

iOS
```swift
extension MainViewController: ATDynamicScreenNameMappable {
    
    func mapScreenName() -> String? {
        "ScreenNameAsYouWant"
    }
}
```

### Send with Screen Parameters Manually
Android
```kotlin
ATEventCenter.send(Event.CLICK, listOf(
    BaseParam.ScreenClass("screenClass"),
    BaseParam.ScreenName("screenName"),
))
```

iOS
```swift
ATEventCenter.Companion().send(event: .click, params: [
    BaseParam.ScreenClass(value: "screenClass"),
    BaseParam.ScreenName(value: "screenName"),
])
```

## TODO
- [x] Publish to maven repository
- [x] Write install guide
- [x] Integration UI with event trigger for iOS
  - [x] LegacyUI
  - [ ] SwiftUI
- [x] Integration UI with event trigger for Android
  - Legacy
    - [x] View, Any
  - [ ] Compose
