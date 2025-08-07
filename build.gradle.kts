plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.sope"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
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
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	// https://mvnrepository.com/artifact/org.springframework/spring-websocket
	implementation("org.springframework:spring-websocket:6.1.14")
	// https://mvnrepository.com/artifact/org.springframework/spring-messaging
	implementation("org.springframework:spring-messaging:6.1.14")


	// https://mvnrepository.com/artifact/com.cloudinary/cloudinary-http44
	implementation("com.cloudinary:cloudinary-http44:1.39.0")

	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")

	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")

	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	implementation("com.github.slugify:slugify:3.0.6")

	// https://mvnrepository.com/artifact/org.mapstruct/mapstruct
	implementation("org.mapstruct:mapstruct:1.6.3")

	// https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
	implementation("io.jsonwebtoken:jjwt:0.12.6")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
