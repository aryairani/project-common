sbtPlugin := true
scalaVersion := "2.10.5"

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.13")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.4")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)
git.useGitDescribe := true

refriedSonatype
