plugins {
	java
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.leesh"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

val asciidoctorExt: Configuration by configurations.creating

extra["springCloudVersion"] = "2022.0.0"

repositories {
	mavenCentral()
}

dependencies {

	// Thymeleaf Start
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	// Thymeleaf End

	// OpenFeign Start
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	// OpenFeign End

	// Spring Rest Docs Start
	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	// Spring Rest Docs End

	// H2 Database
	runtimeOnly("com.h2database:h2")

	// Lombok Start
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// Lombok End

	// Spring Validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Spring Data Jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Spring Boot Start
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Spring Boot End

	//	JWT START
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	// JWT END

	// Spring Security Start
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")
	// Spring Security End

}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
val snippetsDir by extra {
	file("build/generated-snippets")
}

tasks {
	asciidoctor {
		dependsOn(test)
		configurations("asciidoctorExt")
		baseDirFollowsSourceFile()
		inputs.dir(snippetsDir)
	}
	register<Copy>("copyDocument") {
		dependsOn(asciidoctor)
		from(file("build/docs/asciidoc/api-docs.html"))
		into(file("src/main/resources/static/docs"))
	}
	bootJar {
		dependsOn("copyDocument")
	}
}