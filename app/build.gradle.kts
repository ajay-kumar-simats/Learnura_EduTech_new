plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.learnura"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.learnura"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("androidx.media3:media3-ui:1.5.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.airbnb.android:lottie:6.0.0")

    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation ("com.intuit.ssp:ssp-android:1.1.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation ("com.google.guava:guava:31.0.1-android")

    implementation("org.reactivestreams:reactive-streams:1.0.4")

    implementation ("androidx.recyclerview:recyclerview:1.3.2")

    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("androidx.cardview:cardview:1.0.0")

    implementation("org.imaginativeworld.oopsnointernet:oopsnointernet:2.0.0")

    implementation("com.github.AnyChart:AnyChart-Android:1.1.2")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation ("com.android.volley:volley:1.2.1")

    implementation ("org.tensorflow:tensorflow-lite:2.14.0")

    implementation ("com.microsoft.onnxruntime:onnxruntime-android:1.15.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("com.jjoe64:graphview:4.2.2")

    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")


    implementation ("com.itextpdf:itextpdf:5.5.13.3")

    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

    implementation ("androidx.media3:media3-exoplayer:1.5.1")
    implementation ("androidx.media3:media3-ui:1.5.1")






















}