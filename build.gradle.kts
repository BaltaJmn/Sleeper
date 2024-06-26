// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("com.github.triplet.play") version "3.8.6" apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    kotlin("plugin.serialization") version "1.9.0" apply false
}