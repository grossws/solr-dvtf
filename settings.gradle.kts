rootProject.name = "solr-dvtf"

pluginManagement {
  @Suppress("UnstableApiUsage")
  includeBuild("build-logic")
}

plugins {
  id("settings-conventions")
}
