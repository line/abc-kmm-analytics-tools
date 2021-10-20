buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        val agpVersion: String by project
        val kotlinVersion: String by project
        classpath("com.android.tools.build:gradle:$agpVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://repo.navercorp.com/maven-release/")
        }
        maven {
            isAllowInsecureProtocol = true
            url = uri("http://repo.navercorp.com/maven-snapshot/")
        }
    }
}