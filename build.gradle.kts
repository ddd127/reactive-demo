import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.13"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.r2dbc:r2dbc-pool")
    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.flywaydb:flyway-core:7.15.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jooq:jooq:3.17.4")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.zonky.test:embedded-postgres:1.3.1")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.1.1")
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.flywaydb:flyway-core:7.15.0")
        classpath("org.jooq:jooq-codegen:3.17.4")
        classpath("io.zonky.test:embedded-postgres:1.3.1")
        classpath(enforcedPlatform("io.zonky.test.postgres:embedded-postgres-binaries-bom:14.3.0"))
    }
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

tasks.create("jooq-generate") {
    io.zonky.test.db.postgres.embedded.EmbeddedPostgres.builder()
        .start().use { embeddedPostgres ->
            val userName = "postgres"
            val dbName = "postgres"
            val schema = "public"
            org.flywaydb.core.Flyway.configure()
                .locations("filesystem:$projectDir/src/main/resources/db/migration")
                .schemas(schema)
                .dataSource(embeddedPostgres.getDatabase(userName, dbName))
                .load()
                .migrate()
            org.jooq.codegen.GenerationTool.generate(
                org.jooq.meta.jaxb.Configuration().withGenerator(
                    org.jooq.meta.jaxb.Generator().withDatabase(
                        org.jooq.meta.jaxb.Database()
                            .withInputSchema(schema)
                    ).withGenerate(
                        org.jooq.meta.jaxb.Generate()
                    ).withTarget(
                        org.jooq.meta.jaxb.Target()
                            .withPackageName("com.example.reactive.demo.domain.db")
                            .withDirectory("$projectDir/src/main/java/")
                    )
                ).withJdbc(
                    org.jooq.meta.jaxb.Jdbc().withUrl(embeddedPostgres.getJdbcUrl(userName, dbName))
                )
            )
        }
}
