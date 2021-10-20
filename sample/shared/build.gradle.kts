import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

version = "1.0"

val analyticsTools = "com.linecorp:abc-analytics-tools-kmm:1.0.14"

kotlin {
    android()
    ios {
        binaries
            .filterIsInstance<Framework>()
            .forEach {
                it.baseName = "shared"
                it.transitiveExport = true
                it.isStatic = true
                it.export(analyticsTools)
            }
    }

    cocoapods {
        ios.deploymentTarget = "10.0"
        homepage = "https://github.com/line/analytics-tools-kmm/sample/iosApp"
        summary = "Sample with analytics-tools-kmm"

        useLibraries()

        pod("FirebaseAnalytics")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(analyticsTools)
                api(analyticsTools)
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
                implementation("com.google.android.material:material:1.2.1")
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

android {
    compileSdk = 30
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDir("src/androidMain/res")
    defaultConfig {
        minSdk = 24
        targetSdk = 30
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}