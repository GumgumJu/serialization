val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "serialization", // TODO: name your project
    version := "0.1.0-SNAPSHOT",
    developers := List( // TODO: replace the following developer by your team developers
      Developer(
        id    = "Gumgumju",
        name  = "Zhenyu ZHU",
        email = "zhenyu.zhu@edu.esiee.fr",
        url   = url("https://github.com/GumgumJu")
      ),
      Developer(
        id    = "Hao-Li-lih",
        name  = "Hao LI",
        email = "alplhlh@gmail.com",
        url   = url("https://github.com/Hao-Li-lih")
      ),
      Developer(
        id    = "yijun0321",
        name  = "Yijun LIU",
        email = "yijun.liu@edu.esiee.fr",
        url   = url("https://github.com/yijun0321")
      )
    ),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test)
  )
