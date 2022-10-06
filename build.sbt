val scala3Version = "3.2.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "My beautiful project", // TODO: name your project
    version := "0.1.0-SNAPSHOT",
    developers := List( // TODO: replace the following developer by your team developers
      Developer1(
        id1    = "Gumgumju",
        name1  = "Zhenyu ZHU",
        email1 = "zhenyu.zhu@edu.esiee.fr",
        url1   = url("https://github.com/GumgumJu")
      ),
      Developer2(
        id2    = "Hao-Li-lih",
        name2  = "Hao LI",
        email2 = "alplhlh@gmail.com",
        url2   = url("https://github.com/Hao-Li-lih")
      ),
      Developer3(
        id3    = "yijun0321",
        name3  = "Yijun LIU",
        email4 = "yijun.liu@edu.esiee.fr",
        url4   = url("https://github.com/yijun0321")
      )
    ),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test)
  )
