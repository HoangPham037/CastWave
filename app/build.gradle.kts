plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.castwave.castwave"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.castwave.castwave"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://castwave.smartwave247.com:3000/api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"http://castwave.smartwave247.com:3000/api/\"")
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
        android.buildFeatures.buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

    // dagger hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.play.services.cast.framework)
    kapt(libs.hilt.compiler)

    // RX
    implementation (libs.rxkotlin)
    implementation (libs.rxandroid)
    implementation (libs.rxjava)
    implementation (libs.rxbinding.appcompat.v7)

    // retrofit2
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava2)
    implementation (libs.logging.interceptor)

    //event bus
    implementation (libs.eventbus)

    implementation (libs.androidx.swiperefreshlayout)
    implementation (libs.roundedimageview)
    // glide
    implementation (libs.glide)
    implementation(libs.glide.transformations)

    annotationProcessor (libs.compiler)
    implementation(libs.circleimageview)
    //style service
    implementation(libs.androidx.media)
    // login google
    implementation(libs.play.services.auth)
    // Panel 
    implementation(libs.library)

    // check permission
    implementation(libs.dexter)

    //exoplayer
    implementation(libs.exoplayer)
}