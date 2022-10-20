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
    val obtained = for{
      (int, nextInput) <- PseudobinSerde.INT.deserialize(test)
    }yield (int)
    val expected = Try(42)
    assertEquals(obtained, expected)
  }

  test("I want to read 2 int") {
    val string = "         42         99"
    val input = Input(string, 0)
     case class a (int: Int, int2: Int)
    val obtained = for { // for comprehension + try
      (int, nextInput) <- PseudobinSerde.INT.deserialize(input)
      (int2, _) <- PseudobinSerde.INT.deserialize(nextInput)
    } yield (int, int2)
    val expected = Try((42, 99))
    assertEquals(obtained, expected)
  }

  test("Pseudobin to INT failure") {
    val input : Input = Input("         42    4   2",0)
    val obtained = for {
      (int, nextInput) <- PseudobinSerde.INT.deserialize(input)
      (int2, _) <- PseudobinSerde.INT.deserialize(nextInput)
    } yield (int, int2)
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

  test("2 Pseudobin to SHORT(positif) success") {
    val input : Input = Input("    42    89",0)
    val obtained = for {
      (short, nextInput) <- PseudobinSerde.SHORT.deserialize(input)
      (short2, _) <- PseudobinSerde.SHORT.deserialize(nextInput)
    } yield (short, short2)
    val expected = Try((42.toShort,89.toShort))
    assertEquals(obtained, expected)
  }

  test("2 Pseudobin to SHORT(negatif) success") {
    val input : Input = Input("   -42   -99",0)
    val obtained = for {
      (short, nextInput) <- PseudobinSerde.SHORT.deserialize(input)
      (short2, _) <- PseudobinSerde.SHORT.deserialize(nextInput)
    } yield (short, short2)
    val expected = Try((-42.toShort,-99.toShort))
    assertEquals(obtained, expected)
  }

  test("LONG to Pseudobin") {
    val obtained = PseudobinSerde.LONG.serialize(42)
    val expected = "                  42"
    assertEquals(obtained, expected)
  }

  test("3 Pseudobin to LONG success") {
    val input : Input = Input("                  42                   9                 199",0)
    val obtained = for {
      (long, nextInput1) <- PseudobinSerde.LONG.deserialize(input)
      (long2, nextInput2) <- PseudobinSerde.LONG.deserialize(nextInput1)
      (long3, _) <- PseudobinSerde.LONG.deserialize(nextInput2)
    } yield (long, long2, long3)
    val expected = Try((42.toLong, 9.toLong, 199.toLong))
    assertEquals(obtained, expected)
  }

  test("DOUBLE to Pseudobin") {
    val obtained = PseudobinSerde.DOUBLE.serialize(42.24)
    val expected = "                   42.24"
    assertEquals(obtained, expected)
  }

  test("2 Pseudobin to Double success") {
    val input : Input = Input("                   24.42                    1.99",0)
    val obtained = for {
      (double, nextInput) <- PseudobinSerde.DOUBLE.deserialize(input)
      (double2, _) <- PseudobinSerde.DOUBLE.deserialize(nextInput)
    } yield (double, double2)
    val expected = Try((24.42,1.99))
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

  test("2 Pseudobin to BOOLEAN(true)") {
    val input : Input = Input(" truefalse",0)
    val obtained = for {
      (boolean, nextInput) <- PseudobinSerde.BOOLEAN.deserialize(input)
      (boolean2, _) <- PseudobinSerde.BOOLEAN.deserialize(nextInput)
    } yield (boolean, boolean2)
    val expected = Try((true,false))
    assertEquals(obtained, expected)
  }

  test("String to Pseudobin") {
    val obtained = PseudobinSerde.STRING.serialize("hello")
    val expected = "     5hello"
    assertEquals(obtained, expected)
  }

  test("Pseudobin to String success") {
    val input : Input = Input("    11aaaaaaaaaaa     8bbbbbbbb",0)
    val obtained = for {
      (string, nextInput) <- PseudobinSerde.STRING.deserialize(input)
      (string2, _) <- PseudobinSerde.STRING.deserialize(nextInput)
    } yield (string, string2)
    val expected = Try(("aaaaaaaaaaa","bbbbbbbb"))
    assertEquals(obtained, expected)
  }
}
