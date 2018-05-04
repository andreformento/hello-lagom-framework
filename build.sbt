organization in ThisBuild := "com.formento"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `hello-lagom-framework` = (project in file("."))
  .aggregate(`hello-lagom-framework-api`, `hello-lagom-framework-impl`, `hello-lagom-framework-stream-api`, `hello-lagom-framework-stream-impl`)

lazy val `hello-lagom-framework-api` = (project in file("hello-lagom-framework-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-lagom-framework-impl` = (project in file("hello-lagom-framework-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-lagom-framework-api`)

lazy val `hello-lagom-framework-stream-api` = (project in file("hello-lagom-framework-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-lagom-framework-stream-impl` = (project in file("hello-lagom-framework-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-lagom-framework-stream-api`, `hello-lagom-framework-api`)
