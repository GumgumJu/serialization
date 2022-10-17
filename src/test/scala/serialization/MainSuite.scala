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

  test("Pseudobin to INT failure") {
    val test : Input = Input("    4   2",0)
    val obtained = PseudobinSerde.INT.deserialize(test)
    print(obtained)
  }

  test("SHORT(positive) to Pseudobin") {
    val obtained = PseudobinSerde.SHORT.serialize(42)
    val expected = "    42"
    assertEquals(obtained, expected)
  }

  test("SHORT(negative) to Pseudobin") {
    val obtained = PseudobinSerde.SHORT.serialize(-42)
    val expected = "   -42"
    assertEquals(obtained, expected)
  }

  test("LONG to Pseudobin") {
    val obtained = PseudobinSerde.LONG.serialize(42)
    val expected = "                  42"
    assertEquals(obtained, expected)
  }

  test("DOUBLE to Pseudobin") {
    val obtained = PseudobinSerde.DOUBLE.serialize(42.24)
    val expected = "                   42.24"
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

  test("String to Pseudobin") {
    val obtained = PseudobinSerde.STRING.serialize("hello")
    val expected = "     5hello"
    assertEquals(obtained, expected)
  }
}
