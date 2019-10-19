package frank.sat

import org.scalatest.{FlatSpec, Matchers}

class FormulaTest extends FlatSpec with Matchers {

  behavior of "Formula"

  private val satFormula = Formula(Clause(1), Clause(2), Clause(3, 4))
  private val unsatFormula = Formula(Clause(1), Clause(-1))

  it should "check if the formula is K-SAT" in {
    satFormula.isKSat(3) shouldBe true
    satFormula.isKSat(2) shouldBe true
    satFormula.isKSat(1) shouldBe false
  }

  it should "check if the formula is exactly K-SAT" in {
    satFormula.isExactlyKSat(3) shouldBe false
    satFormula.isExactlyKSat(2) shouldBe false
    satFormula.isExactlyKSat(1) shouldBe false
    unsatFormula.isExactlyKSat(1) shouldBe true
  }

  it should "check if the formula is satisfied by a result" in {
    satFormula.isSatisfiedBy(Set(1, 2, 3)) shouldBe true
    satFormula.isSatisfiedBy(Set(1, 2)) shouldBe false
    unsatFormula.isSatisfiedBy(Set(1)) shouldBe false
  }

  it should "propagate a set of literals" in {
    satFormula.propagate(Set(1, -4)) shouldEqual Formula(Clause(2), Clause(3))
    unsatFormula.propagate(Set(1, 2)) shouldEqual Formula(Clause())
  }

  it should "check if a formula is empty" in {
    satFormula.isEmpty shouldBe false
    Formula().isEmpty shouldBe true
  }

  it should "check if a formula contains an empty clause" in {
    satFormula.containsEmptyClause shouldBe false
    Formula(Clause(1), Clause()).containsEmptyClause shouldBe true
  }

  it should "count variables" in {
    satFormula.variableCount shouldEqual 4
    unsatFormula.variableCount shouldEqual 1
  }

  it should "count clauses" in {
    satFormula.clauseCount shouldEqual 3
    unsatFormula.clauseCount shouldEqual 2
  }

  it should "extract unit clause literals" in {
    satFormula.unitClauseLiterals shouldEqual Set(1, 2)
    unsatFormula.unitClauseLiterals shouldEqual Set(1)
  }

  it should "extract pure literals" in {
    satFormula.pureLiterals shouldEqual Set(1, 2, 3, 4)
    unsatFormula.pureLiterals shouldEqual Set()
  }

  it should "have a nice string representation" in {
    unsatFormula.toString shouldEqual "Formula(Clause(1), Clause(-1))"
  }
}
