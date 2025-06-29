import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}


val localProperties = Properties()

val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
    localProperties.load(FileInputStream(localPropertiesFile))
}


android {
    namespace = "ir.amirrezaanari.barandehplanning"
    compileSdk = 35

    defaultConfig {
        applicationId = "ir.amirrezaanari.barandehplanning"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // Adding Task By Voice Base URL
        val voiceTaskBaseUrl = localProperties.getProperty("VOICE_TASK_BASE_URL", "")
        buildConfigField("String", "VOICE_TASK_BASE_URL", "\"$voiceTaskBaseUrl\"")

        // Gemini Chat Base URL
        val geminiChatBaseUrl = localProperties.getProperty("GEMINI_CHAT_BASE_URL", "")
        buildConfigField("String", "GEMINI_CHAT_BASE_URL", "\"$geminiChatBaseUrl\"")


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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
//    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.27.0")
//    implementation(libs.twain)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.room.runtime)
    ksp(libs.ksp.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.compose.markdown)
    implementation(libs.generativeai)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.msz.progress.indicator)
    implementation(libs.persiandate)
    implementation(libs.jalali.datepicker.compose)
    implementation(libs.jalalicalendar)
    implementation(libs.persian.material.datepicker)
    implementation(libs.accompanist.pager)
    implementation(libs.logging.interceptor)

}