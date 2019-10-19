package frank.sat.solvers

import frank.sat._
import frank.sat.{Result, Unsatisfiable}

/**
  * Solve CNF formulas using the DPLL algorithm.
  *
  * The algorithm has a worst-case running time of O(2^n)
  *
  * See: https://en.wikipedia.org/wiki/DPLL_algorithm
  */
object DPLLSolver extends Solver {

  def solverImpl(input: Step): Result =
    branchAt(input, chooseLiteral(input.formula))

  private def chooseLiteral(formula: Formula): Int =
    formula.clauses.find(!_.isEmpty).get.literals.head

  private def branchAt(input: Step, literal: Int) =
    branch(
      resolve(input.propagate(Set(literal))),
      resolve(input.propagate(Set(-literal))))

  private def branch(a: => Result, b: => Result) = {
    val result = a
    if (result != Unsatisfiable) result else b
  }
}
