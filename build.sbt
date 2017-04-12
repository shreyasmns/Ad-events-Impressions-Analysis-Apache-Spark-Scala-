name := "data-processing"

version := "2.0"

scalaVersion := "2.11.8"
scalacOptions += "-target:jvm-1.8"
resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.2" % Provided

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.0.2" % Provided
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.0.2" % Provided
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.0.2" % Provided
libraryDependencies += "com.intentmedia.mario" %% "mario" % "0.1.0"

libraryDependencies ++= Seq("org.atilika.kuromoji" % "kuromoji" % "0.7.7")

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.11"

libraryDependencies += "com.google.doubleclick" % "doubleclick-core" % "0.9.6"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.9"

libraryDependencies += "com.github.scopt" % "scopt_2.10" % "3.5.0"

libraryDependencies += "org.jblas" % "jblas" % "1.2.2"

libraryDependencies += "com.databricks" %% "spark-csv" % "1.5.0"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
