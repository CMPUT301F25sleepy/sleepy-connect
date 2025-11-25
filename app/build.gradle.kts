plugins {
    alias(libs.plugins.android.application)
    //id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.sleepy_connect"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sleepy_connect"
        minSdk = 26 // Changed from 24 this is Android 8.0, launched in 2017
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    // Firebase libraries go below
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.play.services.location)
    implementation(libs.firebase.storage)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.fragment)
    implementation(libs.play.services.maps)
//    testImplementation(libs.junit)
//    testImplementation(libs.junit.junit)
//    testImplementation(libs.junit.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)

    // implemented commented code but with versions
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    implementation("com.google.android.material:material:1.12.0")

    // ZXing for QR code generation + scanning
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // for decoding QR codes in local unit tests
    testImplementation("com.google.zxing:core:3.5.3")
    // Mockito for mocking Android framework classes in tests
    testImplementation("org.mockito:mockito-core:5.12.0")

    // For FragmentScenario to test fragments in isolation
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    
    //for the keywords under the searchbar
    implementation("com.google.android.flexbox:flexbox:3.0.0")
}

configurations {
    // protobuf inside conflicts espresso-contrib conflicts with firebase so we exclude it
    named("androidTestImplementation") {
        exclude(group = "com.google.protobuf", module = "protobuf-lite")
}

   