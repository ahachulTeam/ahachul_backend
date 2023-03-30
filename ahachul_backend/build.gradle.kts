import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
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

    // https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

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