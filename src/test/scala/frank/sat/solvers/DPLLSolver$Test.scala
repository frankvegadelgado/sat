package frank.sat.solvers

import frank.sat._
import org.scalatest.FlatSpec

class DPLLSolver$Test extends FlatSpec with Common {

  private val solver = DPLLSolver

  behavior of "DPLLSolver$"

  it should "solve satisfiable CNF formulas" in {
    sat(solver, Formula(Clause(1), Clause(2), Clause(3, 4)))
    sat(solver, Formula(Clause(1), Clause(-1, 2), Clause(-2, 3)))
    sat(solver, Formula(Clause(1, -2, 3, 4), Clause(-1, -2, -3, 4), Clause(1, 2, 3, -4)))
    sat(solver, Formula())
  }

  it should "solve unsatisfiable CNF formulas" in {
    unsat(solver, Formula(Clause()))
    unsat(solver, Formula(Clause(1), Clause(-1)))
    unsat(solver, Formula(Clause(1, 2), Clause(-1), Clause(-2)))
  }
}
