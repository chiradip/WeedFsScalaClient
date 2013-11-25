object Dependency {
  import sbt._
  import Keys._
  // Versions
  object V {
    val scala         = "2.10.3"
    val typesafe      = "1.0.0"
    val slf4j         = "1.7.5"
    val groovy        = "2.1.2"
    val logback       = "1.0.13"
    val akka          = "2.2.1"
    val scalatest     = "2.0"
    val scalalogging  = "1.0.1"
    val play          = "2.1.1"
    val scalaz        = "7.0.0"
    val atmos         = "1.2.0"
  }

  val scalareflect        = "org.scala-lang"                %   "scala-reflect"         % V.scala

  val typesafeconfig      = "com.typesafe"                  %   "config"                % V.typesafe

  val slf4j               = "org.slf4j"                     %   "slf4j-api"             % V.slf4j
  val logbackClassic      = "ch.qos.logback"                %   "logback-classic"       % V.logback
  val logbackCore         = "ch.qos.logback"                %   "logback-core"       % V.logback
  val groovy              = "org.codehaus.groovy"           %   "groovy-all"            % V.groovy
  val scalalogging        = "com.typesafe"                  %%  "scalalogging-slf4j"    % V.scalalogging exclude("org.scala-lang", "scala-reflect")

  val akkaactor           = "com.typesafe.akka"             %%  "akka-actor"            % V.akka
  val akkakernel          = "com.typesafe.akka"             %%  "akka-kernel"           % V.akka
  val akkaslf4j           = "com.typesafe.akka"             %%  "akka-slf4j"            % V.akka
  val akkaremote          = "com.typesafe.akka"             %%  "akka-remote"           % V.akka
  val akkacluster         = "com.typesafe.akka"             %%  "akka-cluster"          % V.akka

  val scalatest           = "org.scalatest"                 %%  "scalatest"             % V.scalatest   % "test"
  val akkatest            = "com.typesafe.akka"             %%  "akka-testkit"          % V.akka        % "test"

  val play                = "play"                          %   "play_2.10"             % V.play exclude("org.scala-lang", "scala-reflect")

  val atmosTrace          = "com.typesafe.atmos"            %% ("trace-akka-" + "2.2.0") % V.atmos

  val httpcore            = "org.apache.httpcomponents"     %   "httpcore"              % "4.3+"
  val httpmime            = "org.apache.httpcomponents"     %   "httpmime"              % "4.3+"
  val httpclient          = "org.apache.httpcomponents"     %   "httpclient"            % "4.3+"
  val commonsio          = "commons-io"                    %   "commons-io"            % "2.4"
  val gson                = "com.google.code.gson"          %   "gson"                  % "2.2.4"
}

object Dependencies {
  import Dependency._

  val commonDeps   = Seq( slf4j, scalalogging, logbackClassic, groovy, play, typesafeconfig, scalatest, atmosTrace)
  val akkaCommonDeps =  Seq( slf4j, logbackClassic, logbackCore, typesafeconfig, scalatest)
  val akkaDeps     = Seq( akkaactor, akkakernel, akkaremote, akkacluster, akkaslf4j, akkatest )
  val metadatastoreDeps = commonDeps ++ akkaCommonDeps ++ akkaDeps ++ Seq( httpcore, httpmime, httpclient, commonsio, gson )
}

