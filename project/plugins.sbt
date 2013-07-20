addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.2")

resolvers ++= Seq(
  Classpaths.sbtPluginReleases,
  Opts.resolver.sonatypeReleases
)

