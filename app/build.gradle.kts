plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("org.jmailen.kotlinter") version "5.2.0"
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.10"
}

kotlinter {
    ignoreLintFailures = false
    reporters = arrayOf("checkstyle", "plain")
}

android {
    namespace = "com.example.therickandmorty"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.therickandmorty"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
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
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //compose
    implementation(libs.androidx.activity.compose.v182)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android.v171)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil)

    //koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    //voyager
    implementation(libs.voyager.koin)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)

    //mockk
    testImplementation(libs.mockk.agent)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)

    //paggin 3.0
    implementation (libs.androidx.paging.runtime)
    implementation (libs.androidx.paging.compose)

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.androidx.compose.bom.v20231001)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.core.testing)
    androidTestImplementation(libs.runner)
    testImplementation(kotlin("test"))
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test.v173)
    testImplementation(libs.turbine.v100)
    testImplementation(libs.androidx.paging.common)
    testImplementation(libs.mockk.v11310)

}
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    val fileFilter =
        listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "android/**/*.*",

            "**/*Test*.*",

            // UI / Compose
            "**/*Activity*.*",
            "**/*Fragment*.*",
            "**/*Composable*.*",
            "**/*Screen*.*",

            // DI / Generated
            "**/*Hilt*.*",
            "**/*Module*.*",
            "**/*Factory*.*",
            "**/*_Factory*.*",
            "**/*_MembersInjector*.*",

            // Paging
            "**/*PagingSource*.*",
        )

    val kotlinClasses = fileTree(
        mapOf(
            "dir" to "$buildDir/tmp/kotlin-classes/debug",
            "excludes" to fileFilter,
        ),
    )

    val javaClasses = fileTree(
        mapOf(
            "dir" to "$buildDir/intermediates/javac/debug/classes",
            "excludes" to fileFilter,
        ),
    )

    classDirectories.setFrom(files(kotlinClasses, javaClasses))

    sourceDirectories.setFrom(
        files(
            "src/main/java",
            "src/main/kotlin",
        ),
    )

    executionData.setFrom(
        fileTree(buildDir) {
            include(
                "jacoco/testDebugUnitTest.exec",
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            )
        },
    )
}
tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            element = "CLASS"
            includes = listOf(
                "com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel",
                "com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel",
                "com.example.therickandmorty.presentation.viewmodel.SearchViewModel",
                "com.example.therickandmorty.data.repository.CharacterRepositoryImpl",
                "com.example.therickandmorty.domain.usecase.LoadListUseCaseIntImpl",
            )

            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
        }
    }


    classDirectories.setFrom(tasks.named<JacocoReport>("jacocoTestReport").get().classDirectories)
    sourceDirectories.setFrom(tasks.named<JacocoReport>("jacocoTestReport").get().sourceDirectories)
    executionData.setFrom(tasks.named<JacocoReport>("jacocoTestReport").get().executionData)
}

