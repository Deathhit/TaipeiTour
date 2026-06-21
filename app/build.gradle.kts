plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
}

android {
    namespace = "tw.com.deathhit.taipei_tour"
    compileSdk = 37

    defaultConfig {
        applicationId = "tw.com.deathhit.taipei_tour"
        minSdk = 24
        targetSdk = 37
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
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    ksp(libs.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)
    implementation(libs.hilt)

    //Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}