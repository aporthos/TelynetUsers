plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.telynet.telynetusers.core.data"
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)

    implementation(libs.hilt.android)
    "ksp"(libs.hilt.compiler)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.rxjava3)
    implementation(libs.androidx.room.paging)

    implementation(project(":core:domain"))
    implementation(project(":core:models"))
    implementation(project(":core:database"))
}
