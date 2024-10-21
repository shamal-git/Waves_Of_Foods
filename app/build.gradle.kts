plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services) // For Firebase services
}

android {
    namespace = "com.example.adminfoodorderingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.adminfoodorderingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase dependencies
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    // Google Sign-In dependencies
    implementation("com.google.firebase:firebase-auth:21.4.3")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Android support annotations (Optional if needed elsewhere)
    implementation(libs.support.annotations)
    implementation(libs.firebase.storage)

    // Unit testing
    testImplementation(libs.junit)

    // Instrumented testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.credentials:credentials:<latest version>")
    implementation ("androidx.credentials:credentials-play-services-auth:<latest version>")
    implementation ("com.google.android.libraries.identity.googleid:googleid:<latest version>")

    //for glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

}
