package frank.sat.solvers

import frank.sat._

/**
  * Base trait for solvers.
  */
trait Solver[T] {

  /**
    * Solve a formula.
    *
    * @param formula Formula to solve
    * @return Result
    */
  def solve(formula: Formula, param: Option[T] = None): Result =
    resolve(Step(formula))

  /**
    * Simplify the formula and then check if it's satisfiable or unsatisfiable.
    */
  protected def resolve(input: Step): Result =
    simplify(input) match {
      case Step(formula, _) if formula.containsEmptyClause =>
        Unsatisfiable
      case Step(formula, solution) if formula.isEmpty =>
        Satisfiable(solution.toSeq: _*)
      case step: Step =>
        solverImpl(step)
    }

  protected def solverImpl(input: Step): Result

  /**
    * Simplify a formula via unit propagation and pure literal elimination.
    */
  private def simplify(step: Step) =
    step
      .propagate(step.formula.unitClauseLiterals)
      .propagate(step.formula.pureLiterals)
}
