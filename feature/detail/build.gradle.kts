plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.telynet.telynetusers.feature.detail"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.coil)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation(libs.hilt.android)
    "ksp"(libs.hilt.compiler)

    implementation(project(":core:designsystem"))
    implementation(project(":core:models"))
    implementation(project(":core:domain"))
}
