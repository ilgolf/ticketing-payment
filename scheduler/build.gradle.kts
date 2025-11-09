import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":core"))
    implementation(project(":infra"))
    implementation("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter")
}

val bootJar: BootJar by tasks
bootJar.enabled = true