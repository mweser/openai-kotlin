import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("js")
}

dependencies {
    //implementation("com.aallam.openai:openai-client:<version>")
    implementation(project(":openai-client"))
}

kotlin {
    js {
        nodejs {
            binaries.executable()
        }
    }
}
