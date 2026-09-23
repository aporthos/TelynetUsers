plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.telynet.telynetusers.core.domain"
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
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)

    implementation(libs.rxjava)
    implementation(libs.androidx.paging.common)

    implementation(libs.hilt.android)
    "ksp"(libs.hilt.compiler)

    implementation(project(":core:models"))
}
