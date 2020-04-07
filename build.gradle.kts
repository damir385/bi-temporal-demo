plugins {
    id ("org.springframework.boot") version "2.2.6.RELEASE"
    id ("io.spring.dependency-management") version "1.0.9.RELEASE"
    java
    groovy
    `java-library`
}

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

dependencies {

    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.data:spring-data-envers")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.liquibase:liquibase-core")

    compileOnly ("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")

    //runtimeOnly ("com.h2database:h2")
    //runtimeOnly ("com.oracle.ojdbc:ojdbc8")
    runtimeOnly ("org.postgresql:postgresql")

    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor ("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testImplementation("org.spockframework:spock-spring:1.3-groovy-2.5")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    testImplementation( "org.testcontainers:spock:1.13.0")
    testImplementation( "com.playtika.testcontainers:embedded-postgresql:1.43")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
