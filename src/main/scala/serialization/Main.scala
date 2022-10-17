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
object PseudobinSerde{
  val INT = new PseudobinSerde[Int] {
    override def serialize(value: Int): String = {
      val size = value.toString.length
      val blank : String = " " * (11-size)
      blank.concat(value.toString)
    }

    override def deserialize(data: Input): Maybe[Int] = {
      val message : String = data.current(11)
      val tryINT :Maybe[Int] = Try(message.trim.toInt,data)
      tryINT
    }
  }


  val SHORT   = new PseudobinSerde[Short] {
    override def serialize(value: Short): String = {
      val size = value.toString.length
      val blank :String= " " * (6-size)
      blank.concat(value.toString)
    }
    override def deserialize(data: Input): Maybe[Short] = {
      val message : String = data.current(6)
      val trySHORT :Maybe[Short] = Try(message.trim.toShort,data)
      trySHORT
    }
  }
  val LONG    = new PseudobinSerde[Long] {
    override def serialize(value: Long): String = {
      val size = value.toString.length
      blank.concat(value.toString)
      val blank :String= " " * (20-size)
    }
    override def deserialize(data: Input): Maybe[Long] = {
      val message : String = data.current(20)
      val tryLONG :Maybe[Long] = Try(message.trim.toLong,data)
      tryLONG
    }
  }
  val DOUBLE  = new PseudobinSerde[Double] {
    override def serialize(value: Double): String = {
      val size = value.toString.length
      val blank :String= " " * (24-size)
      blank.concat(value.toString)
    }
    override def deserialize(data: Input): Maybe[Double] = {
      val message : String = data.current(24)
      val tryDOUBLE :Maybe[Double] = Try(message.trim.toDouble,data)
      tryDOUBLE
    }
  }

  val BOOLEAN = new PseudobinSerde[Boolean] {
    override def serialize(value: Boolean): String = {
      if(value==true) " true" else "false"
    }
    }
    override def deserialize(data: Input): Maybe[Boolean] = {
      val message: String = data.current(5)
      val tryBOOLEAN : Maybe[Boolean] = Try(message.trim.toBoolean,data)
      tryBOOLEAN
    }
  }

  val STRING  = new PseudobinSerde[String] {
    override def serialize(value: String): String = {
      val size = value.length
      val blank :String= " " * (6-size)
      blank.concat(size.toString).concat(value)
    }
    override def deserialize(data: Input): Maybe[String] = {
      val Message_length: String = data.current(6)
      val str_length : Int = Message_length.trim.toInt
      val message : String = data.current(6+str_length)
      val trySTRING : Maybe[String] = Try(message.substring(6),data)
      trySTRING
    }

  def ARRAY[A](itemSerde: PseudobinSerde[A]) = new PseudobinSerde[List[A]] {
    override def serialize(value: List[A]): String = ???
    override def deserialize(data: Input): Maybe[List[A]] = ???
  }

  def NULLABLE[A](itemSerde: PseudobinSerde[A]): PseudobinSerde[Option[A]] = ???
}
//case class Message(content: String, criticality: Int)
//
//object Message{
//  val serde: PseudobinSerde[Message] =
//    new PseudobinSerde[Message] {
//      override def serializer: String =
//        value =>
//          STRING.toPseudobin(value.content) + INT.toPseudobin(value.criticality)
//
//      override def deserializer(data: Input): Maybe[(Message, Input)] =
//        for {
//          // first get the content and get new input
//          (content, input1) <- STRING.fromPseudoBin(data)
//          // from the new input, get the criticality and another new input
//          (criticality, input2) <- INT.fromPseudoBin(input1)
//        } yield (Message(content, criticality), input2)
//    }
//}

//Use case
/**
 * Operation available in the service.
 *
 * This trait must be implemented by the server part, to compute the
 * result.
 *
 * It must be implemented by the client part, to call the server and its
 * result.
 */
trait OperationService{
  def add(a: Int, b: Int): Option[Int]
  def minus(a: Int, b: Int): Option[Int]
  def multiply(a: Int, b: Int): Option[Int]
  def divide(a: Int, b: Int): Option[Int]
}

/**
 * Available operators, used in operation request, sent by the client.
 */
enum Operator {
  case ADD, MINUS, MULTIPLY, DIVIDE
}

/**
 * Operation request sent by the client to the server.
 *
 * @param id
 *   identification of the request. It is a unique ID bound to this
 *   request. It is also named ''correlation ID''. It can be generated
 *   by using `java.util.UUID.randomUUID().toString`.
 */
case class OperationRequest(id: String, operator: Operator, a: Int, b: Int)

/**
 * Response sent by the server, containing the result.
 *
 * @param id correlation ID, coming from the corresponding request.
 */
case class OperationResponse(id: String, result: Option[Int])
