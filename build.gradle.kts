import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    java
    id("org.springframework.boot") version "2.2.6.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    `java-library`
}


subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "groovy")
    apply(plugin = "io.spring.dependency-management")



    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    configurations {
        compileOnly {
            extendsFrom(configurations.getByName("annotationProcessor"))
        }
    }

    repositories {
        mavenCentral()
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
        compileOnly("org.projectlombok:lombok")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        annotationProcessor("org.projectlombok:lombok")


        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.spockframework:spock-core")
        testImplementation("org.spockframework:spock-spring")
        testImplementation("org.hamcrest:hamcrest-library")
        testImplementation("org.testcontainers:spock")
        testImplementation("org.testcontainers:postgresql")
        //testImplementation("com.playtika.testcontainers:embedded-postgresql")
    }

    val spockVersion: String by project
    val embeddedPostgresVersion: String by project
    val testContainersVersion: String by project

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
        dependencies {
            dependency("org.spockframework:spock-core:${spockVersion}")
            dependency("org.spockframework:spock-spring:${spockVersion}")
            dependency("org.testcontainers:spock:${testContainersVersion}")
            dependency("org.testcontainers:postgresql:${testContainersVersion}")
            //dependency("com.playtika.testcontainers:embedded-postgresql:${embeddedPostgresVersion}")
        }
    }
}





