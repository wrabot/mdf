plugins {
    kotlin("jvm") version "1.7.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("wrabot.competitive:CompetitiveTools:0.17")
    // to test new CompetitiveTools
    //testImplementation("wrabot.competitive:CompetitiveTools") { version { branch = "main" } }
    testImplementation("junit:junit:4.13.2")
}

application {
    mainClass.set("MainKt")
}
