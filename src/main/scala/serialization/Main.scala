package serialization

import scala.util.Try



//Interface
case class Input(data: String, offset: Int) {
  def current(n: Int): String = ???
  def next(n: Int): Input = ???
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

    override def deserialize(data: Input): Maybe[Int] = ???
  }

//  val SHORT = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val LONG = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val DOUBLE = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val BOOLEAN = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val STRING = new PseudobinSerde[String] {
//    override def serialize(value: String): String = {
//      val long = value.lenhgth()
//    }
//
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val ARRAY = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//  val NULLABLE = new PseudobinSerde[Int] {
//    override def serialize(value: A): String = ???
//    override def deserialize(data: Input): Maybe[A] = ???
//  }
//
//
//
//  val SHORT   = ???
//  val LONG    = ???
//  val DOUBLE  = ???
//  val BOOLEAN = ???
//  val STRING  = ???

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