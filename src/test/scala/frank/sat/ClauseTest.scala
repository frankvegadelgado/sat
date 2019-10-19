package frank.sat

import org.scalatest.{FlatSpec, Matchers}

class ClauseTest extends FlatSpec with Matchers {

  behavior of "Clause"

  private val clause = Clause(1, -2, 3, -4)

  it should "extract variables" in {
    clause.variables shouldEqual Seq(1, 2, 3, 4)
  }

  it should "get clause length" in {
    clause.length shouldEqual 4
  }

  it should "check whether a clause is empty" in {
    clause.isEmpty shouldBe false
    Clause().isEmpty shouldBe true
  }

  it should "have a nice string representation" in {
    clause.toString shouldEqual "Clause(1, -2, 3, -4)"
  }

  it should "propagate literals that satisfy the clause" in {
    clause.propagate(Seq(1, 2).toSet) shouldEqual None
  }

  it should "propagate literals that do not satisfy the clause" in {
    clause.propagate(Seq(2, 4).toSet) shouldEqual Some(Clause(1, 3))
  }
}
