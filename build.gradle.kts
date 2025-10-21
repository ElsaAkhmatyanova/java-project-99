plugins {
	application
	checkstyle
	jacoco
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "6.2.0.5505"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Task manager application on Spring Boot"

val mapstructVersion by extra("1.6.3")

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass = "hexlet.code.AppApplication"
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("org.springframework.boot:spring-boot-starter-security")

	compileOnly("org.projectlombok:lombok")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.instancio:instancio-junit:5.5.1")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
	testImplementation ("io.zonky.test:embedded-database-spring-test:2.6.0")
	testImplementation("net.datafaker:datafaker:2.5.2")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
	toolVersion = "10.3.4"
	configFile = rootProject.file("config/checkstyle/checkstyle.xml")
	isIgnoreFailures = false
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

sonar {
	properties {
		property("sonar.projectKey", "ElsaAkhmatyanova_java-project-99")
		property("sonar.organization", "elsaakhmatyanova")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
