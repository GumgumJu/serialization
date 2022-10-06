val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "My beautiful project", // TODO: name your project
    version := "0.1.0-SNAPSHOT",
    developers := List( // TODO: replace the following developer by your team developers
      Developer1(
        id    = "Gumgumju",
        name  = "Zhenyu ZHU",
        email = "zhenyu.zhu@edu.esiee.fr",
        url   = url("https://github.com/GumgumJu")
      ),
      Developer2(
        id    = "Hao-Li-lih",
        name  = "Hao LI",
        email = "alplhlh@gmail.com",
        url   = url("https://github.com/Hao-Li-lih")
      ),
      Developer3(
        id    = "yijun0321",
        name  = "Yijun LIU",
        email = "john.doe@gmail.com",
        url   = url("https://github.com/johndoe")
      )
    ),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test)
  )
