import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    java
    id("org.springframework.boot") version "2.2.6.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    `java-library`
    jacoco
    `project-report`
    `build-dashboard`
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "groovy")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "jacoco")
    apply(plugin = "project-report")
    apply(plugin = "build-dashboard")

    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    configurations {
        compileOnly {
            extendsFrom(configurations.getByName("annotationProcessor"))
        }
    }

    repositories {
        mavenCentral()
        jcenter()
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
        testImplementation("com.athaydes:spock-reports")
        testImplementation("com.aoe:geb-spock-reports")
        testImplementation("org.gebish:geb-spock")
        testImplementation("org.seleniumhq.selenium:selenium-support")
        testImplementation("org.hamcrest:hamcrest-library")
        testImplementation("org.testcontainers:spock")
        testImplementation("org.testcontainers:postgresql")

    }

    val spockVersion: String by project
    val spockReportsVersion: String by project
    val testContainersVersion: String by project
    val gebSpockReportsVersion: String by project
    val gebSpockVersion: String by project
    val seleniumVersion: String by project
    val dataSourceProxyVersion: String by project


    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
        dependencies {
            dependency("org.spockframework:spock-core:${spockVersion}")
            dependency("org.spockframework:spock-spring:${spockVersion}")
            dependency("com.athaydes:spock-reports:${spockReportsVersion}")
            dependency("org.testcontainers:spock:${testContainersVersion}")
            dependency("org.testcontainers:postgresql:${testContainersVersion}")
            dependency("com.aoe:geb-spock-reports:${gebSpockReportsVersion}")
            dependency("org.gebish:geb-spock:${gebSpockVersion}")
            dependency("org.seleniumhq.selenium:selenium-support:${seleniumVersion}")
            dependency("net.ttddyy:datasource-proxy:${dataSourceProxyVersion}")
        }
    }
}

allprojects {
    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = false
            csv.isEnabled = false
            html.isEnabled = true
            html.destination = file("${buildDir}/jacocoHtml")
        }
    }
}


