package serialization

import serialization.PseudobinSerde
import scala.util.{Try,Success,Failure}

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MainSuite extends munit.FunSuite {
  test("INT to Pseudobin") {
    val obtained = PseudobinSerde.INT.serialize(42)
    val expected = "         42"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to INT success") {
    val test : Input = Input("         42",0)
    val obtained = PseudobinSerde.INT.deserialize(test)
    val expected = Success(42,test)
    assertEquals(obtained, expected)
  }

  test("I want to read 2 int") {
    val string = "         42         42"
    val input = Input(string, 0)
     case class a (int: Int, int2: Int)
    val obtained = for { // for comprehension + try
      (int, nextInput) <- PseudobinSerde.INT.deserialize(input)
      (int2, _) <- PseudobinSerde.INT.deserialize(nextInput)
    } yield (int, int2)

    assertEquals(obtained, Try((42, 42)))
  }

  test("Pseudobin to INT failure") {
    val test : Input = Input("    4   2",0)
    val obtained = PseudobinSerde.INT.deserialize(test)
    print(obtained)
  }

  test("SHORT(positif) to Pseudobin") {
    val obtained = PseudobinSerde.SHORT.serialize(42)
    val expected = "    42"
    assertEquals(obtained, expected)
  }

  test("SHORT(negatif) to Pseudobin") {
    val obtained = PseudobinSerde.SHORT.serialize(-42)
    val expected = "   -42"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to SHORT(positif) success") {
    val test : Input = Input("    42",0)
    val obtained = PseudobinSerde.SHORT.deserialize(test)
    val expected = Success(42.toShort,test)
    assertEquals(obtained, expected)
  }

  test("Pseudobin to SHORT(negatif) success") {
    val test : Input = Input("   -42",0)
    val obtained = PseudobinSerde.SHORT.deserialize(test)
    val expected = Success(-42.toShort,test)
    assertEquals(obtained, expected)
  }

  test("LONG to Pseudobin") {
    val obtained = PseudobinSerde.LONG.serialize(42)
    val expected = "                  42"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to LONG success") {
    val test : Input = Input("                  42",0)
    val obtained = PseudobinSerde.LONG.deserialize(test)
    val expected = Success(42.toLong,test)
    assertEquals(obtained, expected)
  }

  test("DOUBLE to Pseudobin") {
    val obtained = PseudobinSerde.DOUBLE.serialize(42.24)
    val expected = "                   42.24"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to Double success") {
    val test : Input = Input("                   24.42",0)
    val obtained = PseudobinSerde.DOUBLE.deserialize(test)
    val expected = Success(24.42,test)
    assertEquals(obtained, expected)
  }

  test("BOOLEAN(true) to Pseudobin") {
    val obtained = PseudobinSerde.BOOLEAN.serialize(true)
    val expected = " true"
    assertEquals(obtained, expected)
  }

  test("BOOLEAN(false) to Pseudobin") {
    val obtained = PseudobinSerde.BOOLEAN.serialize(false)
    val expected = "false"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to BOOLEAN(true)") {
    val test : Input = Input(" true",0)
    val obtained = PseudobinSerde.BOOLEAN.deserialize(test)
    val expected = Success(true,test)
    assertEquals(obtained, expected)
  }

  test("Pseudobin to BOOLEAN(false)") {
    val test : Input = Input("false",0)
    val obtained = PseudobinSerde.BOOLEAN.deserialize(test)
    val expected = Success(false,test)
    assertEquals(obtained, expected)
  }

  test("String to Pseudobin") {
    val obtained = PseudobinSerde.STRING.serialize("hello")
    val expected = "     5hello"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to String success") {
    val test : Input = Input("    11aaaaaaaaaaa",0)
    val obtained = PseudobinSerde.STRING.deserialize(test)
    val expected = Success("aaaaaaaaaaa",test)
    assertEquals(obtained, expected)
  }
}
