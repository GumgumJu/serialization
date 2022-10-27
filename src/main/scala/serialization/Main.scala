package serialization

import scala.util.{Failure, Success, Try}



//Interface
case class Input(data: String, offset: Int) {
  def current(n: Int): String = data.drop(offset).take(n)

  def next(n: Int): Input = copy(offset = offset + n)
}

type Maybe[A] = Try[(A, Input)]

trait PseudobinSerde[A] {
  def serialize(value: A): String
  def deserialize(data: Input): Maybe[A]
  def deserialize(data: String): Maybe[A] = deserialize(Input(data, 0))
}

//Types
object PseudobinSerde {
  val INT = new PseudobinSerde[Int] {
    override def serialize(value: Int): String = {
      val size = value.toString.length
      val blank: String = " " * (11 - size)
      blank.concat(value.toString)
    }

    override def deserialize(data: Input): Maybe[Int] = {
      val message : String = data.current(11)
      val tryINT :Maybe[Int] = Try(message.trim.toInt,data)
      tryINT
    }
  }

  val SHORT = new PseudobinSerde[Short] {
    override def serialize(value: Short): String = {
      val size = value.toString.length
      val blank: String = " " * (6 - size)
      blank.concat(value.toString)
    }

    override def deserialize(data: Input): Maybe[Short] = {
      val message : String = data.current(6)
      for {
        short <- Try(message.trim.toShort)
      } yield (short, data.next(6))
    }
  }

  val LONG = new PseudobinSerde[Long] {
    override def serialize(value: Long): String = {
      val size = value.toString.length
      val blank: String = " " * (20 - size)
      blank.concat(value.toString)
    }

    override def deserialize(data: Input): Maybe[Long] = {
      val message : String = data.current(20)
      for {
        long <- Try(message.trim.toLong)
      } yield (long, data.next(20))
    }
  }

  val DOUBLE = new PseudobinSerde[Double] {
    override def serialize(value: Double): String = {
      val size = value.toString.length
      val blank: String = " " * (24 - size)
      blank.concat(value.toString)
    }

    override def deserialize(data: Input): Maybe[Double] = {
      val message : String = data.current(24)
      for {
        double <- Try(message.trim.toDouble)
      } yield (double, data.next(24))
    }
  }

  val BOOLEAN = new PseudobinSerde[Boolean] {
    override def serialize(value: Boolean): String = {
      if (value) " true" else "false"
    }

    override def deserialize(data: Input): Maybe[Boolean] = {
      val message: String = data.current(5)
      for {
        boolean <- Try(message.trim.toBoolean)
      } yield (boolean, data.next(5))
    }
  }

  val STRING: PseudobinSerde[String] = new PseudobinSerde[String] {
    override def serialize(value: String): String = {
      val size = value.length
      val sizeLength = size.toString.length
      val blank: String = " " * (6 - sizeLength)
      blank.concat(size.toString).concat(value)
    }

    override def deserialize(data: Input): Maybe[String] = {
      val Message_length: String = data.current(6)
      val Message_length_int: Int = Message_length.trim.toInt
      val message: String = data.current(6 + Message_length_int)
      for {
        string <- Try(message.substring(6))
      } yield (string, data.next(6 + Message_length_int))
    }
  }

  def ARRAY[A](itemSerde: PseudobinSerde[A]) = new PseudobinSerde[List[A]] {
    override def serialize(value: List[A]): String = {
      val sizelist = value.length.toShort
      val size = PseudobinSerde.SHORT.serialize(sizelist)
      val as = value.map(a => itemSerde.serialize(a)).mkString("")

      size + as
    }

    override def deserialize(data: Input): Maybe[List[A]] = ???
//  override def deserialize(data: Input): Maybe[List[A]] = {
//      val Message_length: String = data.current(6)
//      val Message_length_int: Int = Message_length.trim.toInt
//      var listReuturn = List[A]
//      for {
//        val a => itemSerde.deserialize()
//        string <- Try(message.substring(6))
//      }yield(listReuturn, data.next(6 + Message_length_int))
//    }
    // foldLeft
  }

  def NULLABLE[A](itemSerde: PseudobinSerde[A])= new PseudobinSerde[Option[A]] {
    override def serialize(value: Option[A]): String = {
      value match {
        case Some(v) => " true" + itemSerde.serialize(v)
        case None => "false"
      }
    }
    // 解释Understand Try, Option and for comprehension
    override def deserialize(data: Input): Maybe[Option[A]] = {
      for {
        (bool, nextInput) <- PseudobinSerde.BOOLEAN.deserialize(data)
        (maybeA, nextNextInput) <-
          if (bool) itemSerde.deserialize(nextInput).map((a, input) => (Some(a), input))
          else Success((None, nextInput))
      } yield (maybeA, nextNextInput)

    }
  }
}

case class Message(content: String, criticality: Int)

object Message{
  val serde: PseudobinSerde[Message] =
    new PseudobinSerde[Message] {
      override def serialize(value:Message): String =
          PseudobinSerde.STRING.serialize(value.content)+PseudobinSerde.INT.serialize(value.criticality)

      override def deserialize(data: Input): Maybe[Message] =
        for {
          // first get the content and get new input
          (content, input1) <- PseudobinSerde.STRING.deserialize(data)
          // from the new input, get the criticality and another new input
          (criticality, input2) <- PseudobinSerde.INT.deserialize(input1)
          A <- Try(Message(content,criticality),input2)
        } yield A
    }
}


