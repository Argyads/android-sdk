plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.argyads"
    compileSdk = 34

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
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}


publishing {
    publications {
        // Register the release publication for publishing
        register<MavenPublication>("release") {
            groupId = "com.github.argyads"
            artifactId = "android-sdk"
            version = "1.0.0"

            // Include the release component from the library module
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            url = uri("https://jitpack.io") // JitPack repository
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Automatically included in the SDK
    api("com.google.android.gms:play-services-ads:24.0.0")
}