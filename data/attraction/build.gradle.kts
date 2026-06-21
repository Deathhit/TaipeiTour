plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "tw.com.deathhit.data.attraction"
    compileSdk = 37

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(":core:app_database"))
    implementation(project(":core:travel_taipei_api"))
    implementation(project(":data:language"))
    implementation(project(":domain"))

    //Coroutine-Test
    androidTestImplementation(libs.jetbrains.koltin.coroutine.test)

    //Paging-Test
    androidTestImplementation(libs.paging.test)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}