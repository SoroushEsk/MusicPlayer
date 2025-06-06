plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("com.google.dagger.hilt.android") version "2.50"
    kotlin("kapt")
}
android {
    namespace = "com.soroush.eskandarie.musicplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.soroush.eskandarie.musicplayer"
        minSdk = 24
        targetSdk = 33
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
        viewBinding = true
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
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
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
    implementation (libs.androidx.media)
//    implementation(libs.androidx.media3.session.v161)

    implementation (libs.androidx.media3.session.v131)
    // Coil
    implementation(libs.coil.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // KSP & Dagger/ Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.window)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.activity)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.android.compiler)

    // Constraint layout
    implementation(libs.constraintlayout.compose)

    // Color thief
    implementation(libs.androidx.palette.ktx)

    // ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.room.compiler)


    // Paging + Room
    implementation (libs.androidx.paging.runtime)
    implementation (libs.room.paging)
    implementation (libs.androidx.paging.compose)

    // Nav Host
    implementation(libs.navigation.compose)


    // Accompanist Permissions
    implementation(libs.accompanist.permissions)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}