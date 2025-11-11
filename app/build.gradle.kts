plugins {
    alias(libs.plugins.android.application)




}

android {
    namespace = "com.abc.knowledgemanagersystems"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.abc.knowledgemanagersystems"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding=true
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

    buildFeatures {
        viewBinding= true //
        // dataBinding true // Không cần nếu chỉ dùng View Binding
    }

}

dependencies {
    implementation(libs.play.services.ads)
    implementation(libs.play.services.maps)
    implementation(libs.gridlayout)
    implementation(libs.recyclerview)
    val room_version = "2.6.1"

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    // Room Database
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("com.auth0.android:jwtdecode:2.0.2")
    // Unit test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

// Retrofit (Giao tiếp API)
    val retrofit_version = "2.9.0" // Dùng 'val' thay cho 'def'
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version") // Dùng hàm implementation() và dấu ngoặc đơn
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

// Encrypted SharedPreferences (Lưu trữ an toàn)
    val security_version = "1.1.0-alpha06" // Dùng 'val'
    implementation("androidx.security:security-crypto:$security_version")

}
