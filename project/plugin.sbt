addSbtPlugin("com.typesafe.akka"  % "akka-sbt-plugin"       % "2.2.1")

addSbtPlugin("net.virtual-void"   % "sbt-dependency-graph"  % "latest.release")

resolvers += Resolver.url(
  "plugin-releases",
  url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "+")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "latest.realese")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "latest.release")
