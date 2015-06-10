sbtPlugin := true
scalaVersion := "2.10.5"

organization := "net.arya"
name := "project-common"

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.13")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.4")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

lazy val refriedSonatype = Seq(
  organization := "net.arya",
  pomExtra := {
    <url>http://github.com/refried/${name.value}</url>
      <licenses>
        <license>
          <name>MIT</name>
          <url>http://opensource.org/licenses/MIT</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:github.com/refried/${name.value}</connection>
        <developerConnection>scm:git:git@github.com:refried/${name.value}</developerConnection>
        <url>github.com/refried/${name.value}</url>
      </scm>
      <developers>
        <developer>
          <id>arya</id>
          <name>Arya Irani</name>
          <url>https://github.com/refried</url>
        </developer>
      </developers>
  }
)
refriedSonatype
enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)
git.useGitDescribe := true

