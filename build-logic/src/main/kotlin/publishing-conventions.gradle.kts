import gradle.kotlin.dsl.accessors._953fddbf04d906afb355ca69451be1ef.publishing
import groovy.namespace.QName
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

plugins {
  id("ws.gross.private-repo-publish")
}

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])

    pom {
      name.set("solr-dvtf")
      description.set("""
        TextField with DocValues support for Apache Solr
      """.trimIndent())
      url.set("https://github.com/grossws/solr-dvtf")

      licenses {
        license {
          name.set("MIT License")
          url.set("https://github.com/grossws/solr-dvtf/blob/master/LICENSE")
        }
      }

      developers {
        developer {
          id.set("grossws")
          name.set("Konstantin Gribov")
          email.set("grossws@gmail.com")
          timezone.set("+3")
        }
      }

      scm {
        url.set("https://github.com/grossws/solr-dvtf")
        connection.set("scm:git:https://githib.com/grossws/solr-dvtf.git")
        developerConnection.set("scm:git:ssh://git@github.com/grossws/solr-dvtf.git")
      }
    }
  }
}
