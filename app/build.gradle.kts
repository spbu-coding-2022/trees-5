plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {

    // Neo4j
    implementation("org.neo4j.driver", "neo4j-java-driver", "5.7.0")

    // JB Exposed
    implementation("org.jetbrains.exposed:exposed-core:0.38.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.1")
    
    // Json Jackson
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

    // Logging
    implementation("org.slf4j:slf4j-nop:1.7.25")

    // Test

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")

}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("prepareKotlinBuildScriptModel"){}
