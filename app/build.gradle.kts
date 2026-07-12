plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android { namespace = "com.example.liquidglassdemo"; compileSdk = 36
    defaultConfig { applicationId = "com.example.liquidglassdemo"; minSdk = 26; targetSdk = 36; versionCode = 1; versionName = "1.0" }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.06.01"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("io.github.kyant0:backdrop:1.0.6")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
