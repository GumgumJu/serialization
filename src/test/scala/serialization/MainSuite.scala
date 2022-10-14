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
}
