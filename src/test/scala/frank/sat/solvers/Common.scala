package frank.sat.solvers

import frank.sat._
import org.scalatest.Matchers

trait Common extends Matchers {

  def sat(solver: Solver[Any], formula: Formula) = {
    solver.solve(formula) should matchPattern {
      case Satisfiable() =>
      case Satisfiable(literals @ _*) if !literals.isEmpty =>
    }
  }

  def unsat(solver: Solver[Any], formula: Formula) = {
    solver.solve(formula) should matchPattern { case Unsatisfiable => }
  }
}
