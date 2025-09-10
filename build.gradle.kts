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

extra["springAiVersion"] = "1.0.1"

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${extra["springAiVersion"]}")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// ===== Spring Boot Core =====
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.web)
	developmentOnly(libs.spring.boot.devtools)

	// ===== Spring Modules =====
	implementation(libs.spring.websocket)
	implementation(libs.spring.messaging)
	implementation(libs.springdoc.openapi)
	implementation(libs.spring.data.redis)


	// ===== Database & Connection Pool =====
	runtimeOnly(libs.postgresql)
	implementation(libs.commons.pool2)

	// ===== Utilities =====
	compileOnly(libs.lombok)
	annotationProcessor(libs.lombok)
	implementation(libs.cloudinary)
	implementation(libs.slugify)
	implementation(libs.spring.dotenv)

	// ===== JWT =====
	implementation(libs.jjwt.api)
	runtimeOnly(libs.jjwt.impl)
	runtimeOnly(libs.jjwt.jackson)
	implementation(libs.jjwt.bom)


	// ===== MapStruct =====
	implementation(libs.mapstruct)
	annotationProcessor(libs.mapstruct.processor)
	annotationProcessor(libs.lombok.mapstruct.binding)
	testAnnotationProcessor(libs.mapstruct.processor)
	testAnnotationProcessor(libs.lombok)
	testCompileOnly(libs.lombok)


	// ===== FlyWay =====
	implementation(libs.flyway.core)
	runtimeOnly(libs.flyway.database.postgresql)


	// ===== FeignClient =====
	implementation(libs.spring.cloud.starter.openfeign)
	implementation(platform(libs.spring.cloud.dependencies))

	// ===== OAuth + Mail =====
	implementation("com.google.auth:google-auth-library-oauth2-http:1.26.0")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	// ===== Thymelaf ======
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.5.4")

	// ===== Gemini AI ======
	implementation("org.springframework.ai:spring-ai-starter-model-openai")

	// ===== Test =====
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.security.test)
//	testRuntimeOnly(libs.junit.platform.launcher)
	testImplementation(libs.mockito)
	testImplementation(libs.h2database)
}

//tasks.withType<Test> {
//	useJUnitPlatform()
//}


apply(from = "$rootDir/gradle/jacoco.gradle.kts")



