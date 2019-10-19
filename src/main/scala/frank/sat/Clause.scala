package frank.sat

/**
  * A disjunction of literals.
  *
  * Literals are either a variable (int > 0) or a negation of a
  * variable (int < 0).
  *
  * @param literals Literals that make up the clause
  */
case class Clause(literals: Int*) {

  /**
    * Extract variables from the clause.
    *
    * @return Collection of variables
    */
  def variables: Seq[Int] = literals map (_.abs)

  /**
    * Get the number of literals in the clause.
    *
    * @return Number of literals
    */
  def length: Int = literals.length

  /**
    * Check whether the clause is empty.
    *
    * @return True if the clause is empty, and false otherwise
    */
  def isEmpty: Boolean = literals.isEmpty

  /**
    * Propagate a set of literals.
    *
    * Since the clause is a disjunction, the presence of any of its literals
    * in the set is enough to satisfy the clause. If the clause can't be
    * satisfied, literals are removed if their negation is present in the set.
    * The clause may be empty after removing such literals which means it can
    * no longer be satisfied.
    *
    * @param set Literals to propagate
    * @return Some(simplifiedClause), or None if the clause was satisfied
    */
  def propagate(set: Set[Int]): Option[Clause] =
    if (isSatisfiedBy(set)) None
    else Some(remove(lit => set.contains(-lit)))

  private def isSatisfiedBy(set: Set[Int]) =
    literals exists set.contains

  private def remove(fn: Int => Boolean): Clause =
    Clause(literals filterNot fn: _*)

  override def toString = s"Clause(${literals.mkString(", ")})"
}
