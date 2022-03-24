plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(plugin(libs.plugins.nebula.release))
  implementation(plugin(libs.plugins.private.repo.publish))
}

kotlinDslPluginOptions {
  jvmTarget.set("11")
}

tasks.withType<JavaCompile>().configureEach {
  options.release.set(11)
}

@Suppress("UnstableApiUsage")
fun DependencyHandler.plugin(plugin: Provider<PluginDependency>) = plugin.get().run {
  create("$pluginId:$pluginId.gradle.plugin:$version")
}
