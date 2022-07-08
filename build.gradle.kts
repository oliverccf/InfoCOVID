import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springdoc.openapi-gradle-plugin") version "1.3.4"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.serasaexperian.infocovid"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.cloud:spring-cloud-starter")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")
    implementation("br.com.caelum.stella:caelum-stella-core:2.1.5")
    implementation("br.com.caelum.stella:caelum-stella-bean-validation:2.1.5")
    implementation("javax.mail:mail:1.4.7")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.9.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.springframework.security:spring-security-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
