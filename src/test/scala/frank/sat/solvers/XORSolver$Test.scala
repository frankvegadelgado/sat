package frank.sat.solvers

import frank.sat.{Clause, Formula}
import org.scalatest.FlatSpec

class XORSolver$Test extends FlatSpec with Common {

  private val solver = XORSolver

  behavior of "XORSolver$"

  it should "solve satisfiable XOR 2CNF formulas" in {
    sat(solver, Formula(Clause(1, 2), Clause(2, 3), Clause(3, 4)))
    sat(solver, Formula(Clause(1, 3), Clause(-1, 2), Clause(2, 3)))
    sat(solver, Formula())
  }

  it should "solve unsatisfiable XOR 2CNF formulas" in {
    unsat(solver, Formula(Clause(1, 2), Clause(-1, 2)))
    unsat(solver, Formula(Clause(1, 2), Clause(2, 3), Clause(1, 3)))
  }
}
