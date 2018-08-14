
name := "untitled6"
 
version := "1.0" 
      
lazy val `untitled6` = (project in file(".")).enablePlugins(PlayScala,PlayJava, PlayEbean)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice,
  "org.apache.cxf" % "cxf-rt-frontend-jaxws" % "3.1.2",
  "org.springframework" % "spring-context" % "5.0.1.RELEASE",
  "org.apache.cxf" % "cxf-rt-bindings-soap" % "3.1.2",
  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1",
  "org.postgresql" % "postgresql" % "9.4.1211",
  "org.webjars" % "jquery" % "3.3.1-1")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      