plugins {
    kotlin("jvm") version "1.7.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("wrabot.competitive:CompetitiveTools:0.28")
    // to test new CompetitiveTools
    //testImplementation("wrabot.competitive:CompetitiveTools") { version { branch = "main" } }
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.7.10")
}
