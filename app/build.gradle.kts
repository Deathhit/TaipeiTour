plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
}

android {
    namespace = "tw.com.deathhit.taipei_tour"
    compileSdk = 34

    defaultConfig {
        applicationId = "tw.com.deathhit.taipei_tour"
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
        debug {
            /*
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
             */
        }

        release {
            isMinifyEnabled = true
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
}

dependencies {
    implementation(project(":core:app_database"))
    implementation(project(":core:app_ui"))
    implementation(project(":core:travel_taipei_api"))
    implementation(project(":data:attraction"))
    implementation(project(":data:attraction_image"))
    implementation(project(":data:event"))
    implementation(project(":data:language"))
    implementation(project(":domain"))
    implementation(project(":feature:attraction_detail"))
    implementation(project(":feature:attraction_gallery"))
    implementation(project(":feature:image_viewer"))
    implementation(project(":feature:navigation"))
    implementation(project(":feature:web_view"))

    //Coroutine-Test
    androidTestImplementation(libs.jetbrains.koltin.coroutine.test)

    //Hilt
    kapt(libs.hilt.compiler)
    implementation(libs.hilt)

    //Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}