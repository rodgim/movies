plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.ksp)
}
val tmdbBaseUrl: String by project
val tmdbApiKey: String by project
val tmdbImageBaseUrl: String by project
val tmdbImage342: String by project
val youtubeVideoUrl: String by project
val youtubeThumbnailUrl: String by project

android {
    namespace = "com.rodgim.movies"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rodgim.movies"
        minSdk = 21
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "TMDB_BASE_URL", tmdbBaseUrl)
        buildConfigField("String", "TMDB_API_KEY", tmdbApiKey)
        buildConfigField("String", "TMDB_IMAGE_BASE_URL", tmdbImageBaseUrl)
        buildConfigField("String", "TMDB_IMAGE_342", tmdbImage342)
        buildConfigField("String", "YOUTUBE_VIDEO_URL", youtubeVideoUrl)
        buildConfigField("String", "YOUTUBE_THUMBNAIL_URL", youtubeThumbnailUrl)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets", "src/androidTest/assets")
        }
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":entities"))
    implementation(project(":usecases"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefreshlayout)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Services
    implementation(libs.play.services.location)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Dexter
    implementation(libs.dexter)

    // Shimmer
    implementation(libs.shimmer)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp3)

    // Koin
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.androidx.core.testing)

    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.koin.test)
    androidTestImplementation(libs.okhttp3.mockwebserver)
    androidTestImplementation(libs.okhttp3.idling.resource)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.idling.resource)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}