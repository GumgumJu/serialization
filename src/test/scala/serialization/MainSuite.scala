package serialization

import serialization.PseudobinSerde

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MainSuite extends munit.FunSuite {
  test("INT to Pseudobin") {
    val obtained = PseudobinSerde.INT.serialize(42)
    val expected = "         42"
    assertEquals(obtained, expected)
  }
}
