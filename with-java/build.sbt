organization in ThisBuild := "com.formento"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `hello-lagom-framework` = (project in file("."))
  .aggregate(`hello-lagom-framework-api`, `hello-lagom-framework-impl`, `hello-lagom-framework-stream-api`, `hello-lagom-framework-stream-impl`)

lazy val `hello-lagom-framework-api` = (project in file("hello-lagom-framework-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `hello-lagom-framework-impl` = (project in file("hello-lagom-framework-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-lagom-framework-api`)

lazy val `hello-lagom-framework-stream-api` = (project in file("hello-lagom-framework-stream-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi
    )
  )

lazy val `hello-lagom-framework-stream-impl` = (project in file("hello-lagom-framework-stream-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaClient,
      lagomLogback,
      lagomJavadslTestKit
    )
  )
  .dependsOn(`hello-lagom-framework-stream-api`, `hello-lagom-framework-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"

def common = Seq(
  javacOptions in compile += "-parameters"
)
