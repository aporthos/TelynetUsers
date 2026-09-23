plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.telynet.telynetusers.core.database"
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

ksp {
    // The DAOs are Java; Kotlin codegen treats their generic types as nullable, which PagingSource rejects
    arg("room.generateKotlin", "false")
}

dependencies {
    testImplementation(libs.junit)

    implementation(libs.hilt.android)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.paging)

    "ksp"(libs.androidx.room.compiler)
    "ksp"(libs.hilt.compiler)
}
