tasks.getByName("bootJar") {
    enabled = false
}

tasks.getByName("jar") {
    enabled = true
}


dependencies {
    implementation(project(":common-libs"))
}