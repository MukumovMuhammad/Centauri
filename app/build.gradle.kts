import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.centauri"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.centauri"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        var properties: Properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String","API_KEY", "\"${properties.getProperty("GEMINI_API_KEY")}\"")

    }
    buildTypes {
        release {
            isMinifyEnabled = true
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
        viewBinding = true
        buildConfig = true
    }


}


dependencies {
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    val nav_version = "2.8.5"

    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")


    implementation("com.google.android.material:material:1.12.0")


    implementation("androidx.core:core-ktx:1.5.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Remove duplicate Firebase dependencies (you added it twice)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


//    toolbar  animation scrolling
    implementation(libs.androidx.appcompat.v110)
    implementation(libs.material.v100)
//    implementation("com.android.support:design:26.1.0")

//    Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Gemeni
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")

    // Ktor Client and JSON Parsing
    implementation("io.ktor:ktor-client-android:2.3.12") {
        exclude(group = "ch.qos.logback", module = "logback-classic") // Exclude Logback from Ktor
    }
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12") // For making HTTP requests
    implementation("io.ktor:ktor-client-serialization:2.3.12") // For JSON parsing
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12") // For kotlinx.serialization
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("com.google.gms:google-services:4.3.15")

    implementation("com.airbnb.android:lottie:6.4.0")


    // Pinch zoom for ImgView
    implementation("com.github.MikeOrtiz:TouchImageView:3.7")


//    Google adMob
    implementation("com.google.android.gms:play-services-ads:24.4.0")



}
