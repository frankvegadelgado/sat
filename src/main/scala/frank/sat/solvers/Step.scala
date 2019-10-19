package frank.sat.solvers


import frank.sat._
/**
  * A partially solved formula.
  */
case class Step(formula: Formula, solution: Set[Int] = Set.empty) {

  /**
    * Propagate literals through the formula.
    *
    * @param literals Literals to propagate
    * @return Step
    */
  def propagate(literals: Set[Int]): Step =
    if (literals.isEmpty) this
    else copy(formula.propagate(literals), solution ++ literals)
}
