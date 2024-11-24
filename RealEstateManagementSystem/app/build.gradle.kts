plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.example.realestatemanagementsystem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.realestatemanagementsystem"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("com.google.firebase:firebase-storage:20.1.0")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("androidx.activity:activity-compose:1.7.0")
    implementation ("androidx.compose.ui:ui:1.4.0")
    implementation ("androidx.compose.material:material:1.4.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation ("io.coil-kt:coil-compose:2.4.0")



    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.storage.ktx)
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.foundation.android)
    ksp(libs.androidx.room.compiler)
    //navbar
    implementation(libs.animated.navigation.bar)
    //Datastore
    implementation (libs.androidx.datastore.preferences)

    implementation (libs.coil.compose)
    implementation (libs.androidx.activity.compose.v172)
    implementation (libs.androidx.ui.v173)
    implementation (libs.androidx.material3.v120)
    implementation (libs.androidx.ui.tooling.preview.v150)
    implementation (libs.androidx.lifecycle.runtime.compose)
    implementation (libs.kotlinx.coroutines.android )// Ensure you have coroutines

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
