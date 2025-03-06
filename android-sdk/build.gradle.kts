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
        register<MavenPublication>("release") {
            groupId = "com.github.argyads"
            artifactId = "android-sdk"
            version = "3.1.0"

            // Include the release component from the library module
            afterEvaluate {
                from(components["release"])
            }

            /* pom {
                withXml {
                    asNode().apply {
                        // Add dependencies manually using XML DSL
                        appendNode("dependencies").apply {
                            appendNode("dependency").apply {
                                appendNode("groupId", "com.google.android.gms")
                                appendNode("artifactId", "play-services-ads")
                                appendNode("version", "24.0.0")
                            }
                        }
                    }
                }
            } */
        }
    }

    repositories {
        maven {
            url = uri("https://jitpack.io") // JitPack repository
        }

        maven {
            url = uri("https://maven.google.com") // Google repository
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
    implementation("com.google.android.gms:play-services-ads:24.0.0")
}