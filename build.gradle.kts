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
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.0.1")
	// Thymeleaf End

	// OpenFeign Start
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.0")
	// OpenFeign End

	// Spring Rest Docs Start
	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
	// Spring Rest Docs End

	// H2 Database
	runtimeOnly("com.h2database:h2:2.1.214")

	// Lombok Start
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	// Lombok End

	// Spring Validation
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.1")

	// Spring Data Jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.1")

	// Spring Boot Start
	implementation("org.springframework.boot:spring-boot-starter-web:3.0.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.1")
	// Spring Boot End

	//	JWT START
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	// JWT END

	// Spring Security Start
	implementation("org.springframework.boot:spring-boot-starter-security:3.0.1")
	testImplementation("org.springframework.security:spring-security-test:6.0.0")
	// Spring Security End

	// Jasypt Start
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")
	// Jasypt End

	// XSS Protect Start
	implementation ("org.apache.commons:commons-text:1.10.0")
	// XSS Protect End

	// Spring AOP Start
	implementation("org.springframework.boot:spring-boot-starter-aop:3.0.1")
	// Spring AOP End

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
