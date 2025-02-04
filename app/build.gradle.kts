plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
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
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.activity:activity-ktx:1.10.0")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.6")

    // Services
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")
    ksp("com.github.bumptech.glide:compiler:4.13.0")

    // Dexter
    implementation("com.karumi:dexter:6.2.2")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Retrofit
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Koin
    implementation("io.insert-koin:koin-android:3.3.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("io.insert-koin:koin-test:3.2.2")
    testImplementation("io.insert-koin:koin-test-junit4:3.2.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("io.insert-koin:koin-test:3.2.2")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.0")
    androidTestImplementation("com.jakewharton.espresso:okhttp3-idling-resource:1.0.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.navigation:navigation-testing:2.8.6")
}