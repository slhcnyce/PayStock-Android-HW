plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

}

android {
    namespace = "com.scy.paystock"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.scy.paystock"
        minSdk = 27
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation ("androidx.camera:camera-camera2:1.2.2")
    implementation ("androidx.camera:camera-lifecycle:1.2.2")
    implementation ("androidx.camera:camera-view:1.2.2")
    implementation ("com.google.mlkit:barcode-scanning:17.1.0")
    implementation ("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")



    // Add other dependencies as needed
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")



}
