import org.jetbrains.kotlin.cli.common.toBooleanLenient
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val isSnapshotUpload = System.getProperty("snapshot").toBooleanLenient() ?: false
val libVersion = "1.1.0"
val gitName = "abc-${project.name}"

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val agpVersion: String by project
        val kotlinVersion: String by project
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("com.android.library")
    id("maven-publish")
    id("signing")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

allprojects {
    ext {
        set("compileSdkVersion", 30)
        set("minSdkVersion", 23)
        set("targetSdkVersion", 30)
    }

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    group = "com.linecorp.abc"
    version = if (isSnapshotUpload) "$libVersion-SNAPSHOT" else libVersion

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }

    afterEvaluate {
        extensions.findByType<PublishingExtension>()?.apply {
            repositories {
                maven {
                    url = if (isSnapshotUpload) {
                        uri("https://oss.sonatype.org/content/repositories/snapshots/")
                    } else {
                        uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    }

                    val sonatypeUsername: String? by project
                    val sonatypePassword: String? by project

                    println("sonatypeUsername, sonatypePassword -> $sonatypeUsername, ${sonatypePassword?.masked()}")

                    credentials {
                        username = sonatypeUsername ?: ""
                        password = sonatypePassword ?: ""
                    }
                }
            }

            publications.withType<MavenPublication>().configureEach {
                artifact(javadocJar.get())

                pom {
                    name.set(artifactId)
                    description.set("AnalyticsTools with Kotlin Multiplatform Mobile. Compatible with iOS and Android")
                    url.set("https://github.com/line/$gitName")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            name.set("LINE Corporation")
                            email.set("dl_oss_dev@linecorp.com")
                            url.set("https://engineering.linecorp.com/en/")
                        }
                        developer {
                            id.set("pisces")
                            name.set("Steve Kim")
                            email.set("pisces@linecorp.com")
                        }
                    }
                    scm {
                        connection.set("scm:git@github.com:line/$gitName.git")
                        developerConnection.set("scm:git:ssh://github.com:line/$gitName.git")
                        url.set("http://github.com/line/$gitName")
                    }
                }
            }
        }

        extensions.findByType<SigningExtension>()?.apply {
            val publishing = extensions.findByType<PublishingExtension>() ?: return@apply
            val signingKey: String? by project
            val signingPassword: String? by project

            println("signingKey, signingPassword -> ${signingKey?.slice(0..9)}, ${signingPassword?.masked()}")

            isRequired = !isSnapshotUpload
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }

        tasks.withType<Sign>().configureEach {
            onlyIf { !isSnapshotUpload }
        }
    }
}

kotlin {
    cocoapods {
        ios.deploymentTarget = "10.0"
        homepage = "https://github.com/line/$gitName"
        summary = "Analytics Tools for Kotlin Multiplatform Mobile iOS and android"
    }

    val enableGranularSourceSetsMetadata = project.extra["kotlin.mpp.enableGranularSourceSetsMetadata"]?.toString()?.toBoolean() ?: false
    if (enableGranularSourceSetsMetadata) {
        val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64
        iosTarget("ios") { }
    } else {
        ios()
    }

    android {
        publishAllLibraryVariants()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt")
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
                implementation("androidx.startup:startup-runtime:1.0.0")
                implementation("com.google.code.gson:gson:2.8.9")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("org.mockito.kotlin:mockito-kotlin:2.2.10")
                implementation("androidx.test:core:1.0.0")
                implementation("androidx.test:runner:1.1.0")
                implementation("org.robolectric:robolectric:4.5.1")
            }
        }
        val iosMain by getting
        val iosTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
    }
}

android {
    val compileSdkVersion = project.ext.get("compileSdkVersion") as Int
    val minSdkVersion = project.ext.get("minSdkVersion") as Int
    val targetSdkVersion = project.ext.get("targetSdkVersion") as Int

    compileSdk = compileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["test"].assets.srcDir("src/androidTest/assets")
    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

fun String.masked() = map { "*" }.joinToString("")