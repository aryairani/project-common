package net.arya

import sbt._, sbt.dsl._, sbt.Keys._

import com.typesafe.sbt.{GitVersioning, GitPlugin}
import wartremover.WartRemover
import xerial.sbt.Sonatype

object ProjectCommon extends sbt.AutoPlugin { common =>
  override def trigger = allRequirements
  override def requires = WartRemover && GitPlugin && Sonatype
  override val projectSettings = Seq(
    scalaVersion := "2.11.6",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",       // yes, this is 2 args
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture"
    ),
    libraryDependencies += "com.lihaoyi" % "ammonite-repl" % "0.3.2" % "test" cross CrossVersion.full,
    initialCommands in (Test, console) := """ammonite.repl.Repl.run("")"""
  ) ++ kindProjector

  // https://github.com/non/cats
  lazy val cats = Seq(
    libraryDependencies += "org.spire-math" %% "cats-state" % "0.1.0-SNAPSHOT",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

  // https://github.com/non/kind-projector
  lazy val kindProjector = Seq(
    resolvers += "bintray/non" at "http://dl.bintray.com/non/maven",
    addCompilerPlugin("org.spire-math" % "kind-projector" % "0.5.4" cross CrossVersion.binary)
  )

  // https://github.com/HairyFotr/linter
  lazy val linter = Seq(
    resolvers += "Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases",
    addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1-SNAPSHOT")
  )

  object autoImport {
    val macroParadise = addCompilerPlugin("org.scalamacros" %% "paradise" % "2.0.1" cross CrossVersion.full)
    val linter = common.linter

    val scalaReflect = libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

    val noPredef = scalacOptions += "-Yno-predef"
    val noImports = scalacOptions in compile += "-Yno-imports" // will break REPL if not restricted to compile

    val warnUnusedImport = scalacOptions in compile += "-Ywarn-unused-import"
    val fatalWarnings = scalacOptions in compile += "-Xfatal-warnings"
    val picky = Seq(warnUnusedImport, fatalWarnings)

    val catsSnapshot = common.cats
    def cats(module: String, gitReference: String = "#master"): ProjectRef =
      ProjectRef(uri(s"git@github.com:non/cats.git$gitReference"), s"cats-$module")

    def scalaz(module: String): ModuleID = "org.scalaz" %% s"scalaz-$module" % "7.1.2"

    val monocle = Seq(
      macroParadise,
      resolvers += Resolver.sonatypeRepo("releases"),
      libraryDependencies ++= {
        val libraryVersion = "1.1.1"
        Seq(
          "com.github.julien-truffaut"  %% "monocle-core"     % libraryVersion,
          "com.github.julien-truffaut"  %%  "monocle-generic" % libraryVersion,
          "com.github.julien-truffaut"  %%  "monocle-macro"   % libraryVersion
        )
      }
    )

    val gitDescribeVersioning = Seq(enablePlugins(GitVersioning), GitPlugin.autoImport.git.useGitDescribe := true)

    // todo: a way to include dependency as git submodule
    // https://github.com/rossabaker/scataz

    val refriedSonatype = common.refriedSonatype
  }

  lazy val refriedSonatype =
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
}
