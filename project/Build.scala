import com.typesafe.sbt.SbtAtmos._
import sbt._
import sbt.Keys._

import akka.sbt.AkkaKernelPlugin
import akka.sbt.AkkaKernelPlugin.{ Dist, outputDirectory, distJvmOptions, distMainClass }

import com.typesafe.sbt.SbtAtmos.{ Atmos, atmosSettings }

object WeedFSClientBuild extends Build {

  val defaultSettingsWithExport = defaultSettings ++ Seq( exportJars := true)

  lazy val WeedFSClient = Project(
    id = "WeedFSScalaClient",
    base = file("."),
    settings = defaultSettingsWithExport ++ Seq(
      libraryDependencies ++= Dependencies.metadatastoreDeps)
  ).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "Paperless Club",
    version      := "0.1",
    scalaVersion := "2.10.3",
    crossPaths   := false,
    organizationName := "Paperless Club, PLC",
    organizationHomepage := Some(url("http://www.chiradip.com"))
  )

  lazy val defaultSettings = buildSettings ++ Seq(
    resolvers += "Typesafe Release Repo"  at "http://repo.typesafe.com/typesafe/releases/",
    //resolvers += "Typesafe Snapshot Repo" at "http://repo.typesafe.com/typesafe/snapshots/",
    resolvers += "Sonatype Release Repo"  at "https://oss.sonatype.org/content/repositories/releases/",
    resolvers += "snmp4j Repo" at "https://oosnmp.net/dist/release/",
    resolvers += Opts.resolver.sonatypeSnapshots,

    // compile options
    scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature", "-language:postfixOps"),
    javacOptions  ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")
  )
}