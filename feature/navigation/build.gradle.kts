plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
}

android {
    namespace = "tw.com.deathhit.feature.navigation"
    compileSdk = 37

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":core:app_ui"))
    implementation(project(":feature:attraction_list"))
    implementation(project(":feature:event_list"))
    implementation(project(":feature:set_day_night"))
    implementation(project(":feature:set_language"))

    //Coroutine-Test
    androidTestImplementation(libs.jetbrains.koltin.coroutine.test)

    //Hilt
    ksp(libs.hilt.compiler)
    implementation(libs.hilt)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}