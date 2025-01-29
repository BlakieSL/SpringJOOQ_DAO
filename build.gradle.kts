plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "9.0"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    val jooqStrategy by creating {
        java.srcDirs("src/jooqStrategy/java")
    }
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:3.19.18")

    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    jooqGenerator("org.jooq:jooq-codegen:3.19.18")
    jooqGenerator("org.jooq:jooq-meta:3.19.18")
    jooqGenerator("mysql:mysql-connector-java:8.0.33")
    jooqGenerator(sourceSets["jooqStrategy"].output)

    "jooqStrategyImplementation"("org.jooq:jooq-codegen:3.19.18")
    "jooqStrategyImplementation"("org.jooq:jooq-meta:3.19.18")
    "jooqStrategyImplementation"("org.jooq:jooq:3.19.18")
    "jooqStrategyImplementation"("mysql:mysql-connector-java:8.0.33")
}

jooq {
    version.set("3.19.18")
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3308/example"
                    user = "root"
                    password = "mainpass"
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    strategy.name = "org.example.springjooq.config.CustomJooqGeneratorStrategy"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "example"
                        includes = ".*"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "org.example.jooq.generated"
                        directory = "src/main/generated"
                    }
                    strategy.name = "org.example.springjooq.config.CustomJooqGeneratorStrategy"
                }
            }
        }
    }
}

val buildJOOQImage by tasks.registering(Exec::class) {
    group = "jooq"
    description = "Builds a custom Docker image for JOOQ DB"
    workingDir("./src/main/resources/docker-build")
    commandLine("docker", "build", "-t", "jooq-mysql-db:latest", ".")
    doFirst {
        println("Building JOOQ MySQL Docker Image...")
    }
}

val startJOOQContainer by tasks.registering(Exec::class) {
    group = "jooq"
    description = "Starts the MySQL Docker container for JOOQ"

    commandLine("docker-compose", "-f", "./src/main/resources/docker-build/docker-compose.yaml", "up", "-d")

    doFirst {
        println("Starting MySQL container using Docker Compose...")
    }
}

val stopJOOQContainer by tasks.registering(Exec::class) {
    group = "jooq"
    description = "Stops and removes the MySQL Docker container"

    commandLine("docker-compose", "-f", "./src/main/resources/docker-build/docker-compose.yaml", "down")

    doFirst {
        println("Stopping MySQL container...")
    }
}

val waitForJOOQDB by tasks.registering {
    group = "jooq"
    description = "Waits until the MySQL container is ready"

    doLast {
        println("Waiting for MySQL to be ready...")
        ant.withGroovyBuilder {
            "waitfor"(
                "maxwait" to "20", "maxwaitunit" to "second",
                "checkevery" to "500", "checkeveryunit" to "millisecond"
            ) {
                "socket"("server" to "localhost", "port" to "3308")
            }
        }
    }
}


startJOOQContainer {
    dependsOn(buildJOOQImage)
}

waitForJOOQDB {
    dependsOn(startJOOQContainer)
    mustRunAfter(startJOOQContainer)
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    dependsOn(waitForJOOQDB, "compileJooqStrategyJava")
    mustRunAfter(waitForJOOQDB)
}

tasks.withType<Test> {
    useJUnitPlatform()
    dependsOn("generateJooq")
}

