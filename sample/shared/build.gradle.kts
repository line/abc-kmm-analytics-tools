import org.jetbrains.kotlin.gradle.plugin.mpp.Framework

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

version = "1.0"

val analyticsTools = "com.linecorp.abc:kmm-analytics-tools:1.0.15"

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
        homepage = "https://github.com/line/abc-kmm-analytics-tools/sample/iosApp"
        summary = "Sample with abc-kmm-analytics-tools"

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
        minSdk = 21
        targetSdk = 30
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}