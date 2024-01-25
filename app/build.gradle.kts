plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
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
    implementation(project(":data:language"))
    implementation(project(":domain"))
    implementation(project(":feature:attraction_detail"))
    implementation(project(":feature:attraction_gallery"))
    implementation(project(":feature:attraction_list"))
    implementation(project(":feature:image_viewer"))

    //Coroutine-Test
    val coroutineVersion = "1.7.3"
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion")

    //Hilt
    val hiltVersion = "2.50"
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

hilt {
    enableAggregatingTask = true
}

kapt {
    //Hilt: Allow references to generated code
    correctErrorTypes = true
}