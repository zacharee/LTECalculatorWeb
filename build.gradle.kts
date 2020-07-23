import org.gradle.kotlin.dsl.precompile.PrecompiledProjectScript.NullPluginDependencySpec.version

plugins {
    id("org.jetbrains.kotlin.js") version("1.3.72")
}

group = "tk.zwander"
version("1.0-SNAPSHOT")

repositories {
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlin-js-wrappers/")
//    maven("https://dl.bintray.com/cfraser/muirwik")
}

dependencies {
    implementation(kotlin("stdlib-js"))

    implementation("org.jetbrains:kotlin-react:16.13.1-pre.110-kotlin-1.3.72")
    implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.110-kotlin-1.3.72")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.1")
    implementation(npm("react", "16.13.1"))
    implementation(npm("react-dom", "16.13.1"))
    implementation(npm("react-is", "16.13.1"))

    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.110-kotlin-1.3.72")
    implementation(npm("styled-components"))
    implementation(npm("inline-style-prefixer"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")

    implementation("com.ionspin.kotlin:bignum:0.1.5")

//    implementation(npm("@material-ui/core", "^4.9.14"))
//    implementation("com.ccfraser.muirwik:muirwik-components:0.5.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test-js")
}

kotlin.target.browser { }