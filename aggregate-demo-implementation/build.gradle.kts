plugins {
    id ("org.springframework.boot")
}

dependencies {
    implementation ("org.liquibase:liquibase-core")
    implementation (project(":spring-data-jpa-bitemporal"))
    implementation ("net.ttddyy:datasource-proxy")

    runtimeOnly ("org.postgresql:postgresql")
}
