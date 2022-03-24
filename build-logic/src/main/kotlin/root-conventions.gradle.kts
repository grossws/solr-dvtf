import nebula.plugin.release.ReleaseExtension
import nebula.plugin.release.ReleasePlugin

plugins {
  id("ws.gross.release-approve")
}

// not in plugins block since it will fail because git directory is absent
// in `gradle-kotlin-dsl-accessors` synthetic project
apply<ReleasePlugin>()
