rootProject.name = "solr-dvtf-build-logic"

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }

  versionCatalogs {
    create("libs") { from(files("../gradle/libs.versions.toml")) }
  }
}
