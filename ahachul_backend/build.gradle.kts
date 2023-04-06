import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.7.22"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.7.22"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

noArg {
    annotation("jakarta.persistence.Entity")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

group = "backend.team"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val asciidoctorExt: Configuration by configurations.creating

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // https://mvnrepository.com/artifact/io.github.microutils/kotlin-logging/3.0.5
    implementation("io.github.microutils:kotlin-logging:3.0.5")

    // https://mvnrepository.com/artifact/org.springframework.restdocs/spring-restdocs-mockmvc/3.0.0
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api/5.9.2
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")

    // https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl/0.11.5
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // https://mvnrepository.com/artifact/org.springframework/spring-webflux
    implementation("org.springframework:spring-webflux:6.0.7")

    // https://mvnrepository.com/artifact/com.h2database/h2
    testImplementation("com.h2database:h2:2.1.214")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val snippetsDir = file("build/generated-snippets")

    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    asciidoctor {
        configurations(asciidoctorExt.name)

        doFirst {
            delete("src/main/resources/static/docs")
        }

        forkOptions {
            jvmArgs("--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens", "java.base/java.io=ALL-UNNAMED")
        }

        inputs.dir(snippetsDir)

        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }

        sources {
            include("**/index.adoc")
        }
        dependsOn(test)
    }

    build {
        dependsOn(asciidoctor)
    }
}

tasks.register<Copy>("copySecret") {
    from("./ahachul_secret") {
        include("*.yml")
    }
    into("./src/main/resources")
}

tasks.named("compileJava") {
    dependsOn("copySecret")
}