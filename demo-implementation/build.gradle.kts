plugins {
    id ("org.springframework.boot")
}

dependencies {
    implementation ("org.liquibase:liquibase-core")
    implementation (project(":spring-data-jpa-bitemporal"))

    runtimeOnly ("org.postgresql:postgresql")
}

