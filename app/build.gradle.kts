plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.retailrevamp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.retailrevamp"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions{
        exclude("META-INF/DEPENDENCIES")
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.code.gson:gson:2.8.8")
    testImplementation("junit:junit:4.13.2")


//    implementation ("com.google.api-client:google-api-client:2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")


    implementation ("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.api-client:google-api-client-android:1.32.1")
//    implementation ("com.google.apis:google-api-services-sheets:v4-rev581-1.25.0")

//    implementation ("com.google.http-client:google-http-client-gson:1.41.1")
//    implementation ("com.google.apis:google-api-services-drive:v3-rev20220405-1.35.0")


//    implementation ("com.android.volley:volley:1.1.1")
}