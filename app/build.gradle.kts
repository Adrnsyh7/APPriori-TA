plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.submission.tesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.submission.tesapp"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))



   implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("io.coil-kt:coil:2.5.0")
    //implementation("com.android.tools.build:gradle:7.1.0-alpha12")

    //card
    implementation("androidx.cardview:cardview:1.0.0")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth")

    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation("androidx.activity:activity:1.10.1")


    //implementation("com.google.firebase:firebase-appdistribution-gradle:5.1.1")
   //implementation("com.google.firebase:firebase-auth-ktx:23.2.0")

    //bar
//    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //preference
    val preference_version = "1.2.0"
    implementation("androidx.preference:preference-ktx:$preference_version")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.room:room-ktx:2.5.1")

    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

//    implementation("androidx.work:work-runtime:2.8.1")
//    implementation("com.loopj.android:android-async-http:1.4.10")
//


    implementation ("com.google.android.material:material:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}