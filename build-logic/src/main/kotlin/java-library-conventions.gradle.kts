plugins {
  `java-library`
  id("publishing-conventions")
}

java {
  withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
  options.release.set(8)
}

@Suppress("UnstableApiUsage")
tasks.named<ProcessResources>("processResources") {
  into(".") {
    from(layout.projectDirectory)
    include("LICENSE")
  }
}
