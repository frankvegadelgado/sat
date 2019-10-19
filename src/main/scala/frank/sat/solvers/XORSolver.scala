package frank.sat.solvers

import frank.sat._



/**
  * Solve XOR 2-SAT CNF formulas by converting
  * them to 2SAT CNF formulas
  *
  * The algorithm has polynomial running time.
  */
object XORSolver extends Solver[Any] {

  /**
    * Solve a formula.
    *
    * @param formula Formula to solve
    * @return Result
    */
  override def solve(formula: Formula, param: Option[Any] = None): Result =
    solverImpl(Step(formula))

  protected def solverImpl(input: Step): Result = {
    require(input.formula.isExactlyKSat(2), "formula is not exactly XOR 2-SAT")

    val clauses = conversion(input.formula)
    val formula = Formula(clauses: _*)

    SCCSolver.solve(formula)
  }

  // Conversion to 2SAT set of Clauses
  private def conversion(formula: Formula) =
    formula.clauses flatMap { case Clause(a, b) => Seq(Clause(a, b), Clause(-a, -b)) }


}
