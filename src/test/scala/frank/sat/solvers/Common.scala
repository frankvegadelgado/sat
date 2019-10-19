package frank.sat.solvers

import frank.sat._
import org.scalatest.Matchers

trait Common extends Matchers {

  def sat(solver: Solver, formula: Formula) = {
    solver.solve(formula) should matchPattern {
      case Satisfiable(literals @ _*)
        if formula.isSatisfiedBy(literals.toSet) =>
    }
  }

  def unsat(solver: Solver, formula: Formula) = {
    solver.solve(formula) should matchPattern { case Unsatisfiable => }
  }
}
