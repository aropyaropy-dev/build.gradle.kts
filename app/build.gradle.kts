plugins {
    id("com.android.application")
}

android {
    namespace = "com.aiwolfie.iprotator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aiwolfie.iprotator"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("com.wireguard.android:tunnel:1.0.20250531")
}
