package frank.sat.parsers

import frank.sat._
import org.scalatest.{FlatSpec, Matchers}

class DimacsFormula$Test extends FlatSpec with Matchers {

  behavior of "DimacsFormula$"

  it should "parse empty formulas" in {
    DimacsFormula.parse(Seq("p cnf 0 0")) should matchPattern { case Formula() => }
  }

  it should "parse CNF formulas" in {
    val lines = Seq(
      "c a comment",
      "c another comment",
      "p cnf 3 2",
      "1 2 0",
      "3 -1 0"
    )
    DimacsFormula.parse(lines) should matchPattern {
      case Formula(Clause(1, 2), Clause(3, -1)) => }
  }

  it should "dump CNF formulas" in {
    val formula = Formula(Clause(1, 2), Clause(3, -1))
    DimacsFormula.dump(formula) shouldEqual Seq(
      "p cnf 3 2",
      "1 2 0",
      "3 -1 0")
  }
}
