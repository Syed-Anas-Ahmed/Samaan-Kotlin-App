plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    kotlin("plugin.parcelize")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

android {
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    namespace = "com.example.samaan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.samaan"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    // Dependencies go here

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.50")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // Navigation Safe Args
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    //Kotin Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")


    //Default Deps
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //custom

    //loading button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.14.2")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //viewpager2 indicatior
    implementation("com.tbuonomo:dotsindicator:5.0")

    //stepView
    implementation ("com.github.shuhart:stepview:1.5.1")

}