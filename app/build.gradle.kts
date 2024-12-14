plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.volatoon"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.volatoon"
        minSdk = 26
        targetSdk = 35
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation ("androidx.navigation:navigation-compose:2.8.4")

//    compose view model
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

//    Network calls
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

//    Json to Kotlin object Mapping
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

//    image loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-android:1.3.1")
    implementation("androidx.room:room-runtime-android:2.7.0-alpha11")
    implementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("androidx.compose.ui:ui-tooling")
    implementation (platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth")
    implementation ("androidx.credentials:credentials:1.3.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation ("androidx.credentials:credentials:1.3.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")

    // Add Google Identity Services
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}