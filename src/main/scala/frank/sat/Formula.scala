package frank.sat

/**
  * A boolean formula in conjunctive normal form.
  *
  * @param clauses Disjunctions
  */
case class Formula(clauses: Clause*) {


  /**
    * Return true if the variables are all from 1 to n
    * @return
    */
  def isVariablesFromOneToN: Boolean ={
    val setVar = variables
    val n = setVar.max
    val setRange = (1 to n).toSet
    setVar.equals(setRange)
  }

  /**
    * Return true if the formula does not contain a tautology clause
    * @return
    */
  def freeTautology = clauses forall (!_.isTautology)
  /**
    * Check if the formula is K-SAT, i.e. is made up of clauses with
    * at most K literals.
    *
    * @param k Maximum number of literals per clause
    * @return True if the formula is K-SAT, and false otherwise
    */
  def isKSat(k: Int): Boolean =
    clauses forall (_.length <= k)

  /**
    * Check if the formula is exactly K-SAT, i.e. is made up of clauses with
    * exactly K literals.
    *
    * @param k Maximum number of literals per clause
    * @return True if the formula is exactly K-SAT, and false otherwise
    */
  def isExactlyKSat(k: Int): Boolean =
    clauses forall (_.length == k)

  /**
    * Check if the formula has at most K positive literals for each clause
    *
    * @param k Maximum number of positive literals per clause
    * @return True if the verification is positive, and false otherwise
    */
  def isExactlyKPosVar(k: Int): Boolean =
  clauses forall (_.literals.count(_ > 0) <= k)


  /**
    * Check if the formula is Monotone, i.e. is made up of clauses with
    * all positive literals.
    *
    * @return True if the formula is Monotone, and false otherwise
    */
  def isMonotoneSat: Boolean =
    clauses forall (_.literals.forall(_ >= 0))

  /**
    * Check whether a set of literals satisfies the formula.
    *
    * @param literals Literals to check
    * @return True if the literals satisfy the formula, and false otherwise
    */
  def isSatisfiedBy(literals: Set[Int]): Boolean =
    propagate(literals).isEmpty

  /**
    * Propagate a set of literals.
    *
    * @param set Set of literals to propagate
    * @return Simplified formula
    */
  def propagate(set: Set[Int]): Formula =
    Formula(clauses flatMap (_.propagate(set)): _*)

  /**
    * Check if the formula is empty.
    *
    * @return True if the formula is empty, and false otherwise
    */
  def isEmpty: Boolean = clauses.isEmpty

  /**
    * Check if the formula contains an empty clause.
    *
    * @return True if an empty clause was found, and false otherwise
    */
  def containsEmptyClause: Boolean =
    clauses exists (_.isEmpty)

  /**
    * Count unique variables in the formula.
    *
    * @return Unique variable count
    */
  def variableCount: Int =
    clauses.flatMap(_.variables).toSet.size

  /**
    * Unique variables in the formula.
    *
    * @return Unique variable count
    */
  def variables: Set[Int] =
    clauses.flatMap(_.variables).toSet

  /**
    * Count clauses in the formula.
    *
    * @return Clause count
    */
  def clauseCount: Int = clauses.length

  /**
    * Extract unit clause literals from the formula.
    *
    * Unit clause literals are the literals from clauses that contain
    * exactly one literal.
    *
    * If two unit clauses contradict one another then this method
    * omits the contradictory literal.
    *
    * Example:
    *
    *   val formula = Formula(Clause(1), Clause(2), Clause(-3), Clause(-1))
    *
    *   formula.unitClauseLiterals // => Set(1, 2, -3)
    *
    * @return Unit clause literals
    */
  def unitClauseLiterals: Set[Int] =
    removeContradictions(clauses.flatMap {
      case Clause(lit) => Some(lit)
      case _ => None
    }.toSet)

  private def removeContradictions(literals: Set[Int]) =
    literals -- findContradictions(literals)

  private def findContradictions(literals: Set[Int]) =
    literals.filter { lit => lit < 0 && literals.contains(-lit) }

  /**
    * Extract pure literals from the formula.
    *
    * Pure literals are literals that appear in the formula with
    * one polarity only.
    *
    * Example:
    *
    *   val formula = Formula(Clause(1, 2, -3), Clause(-1, 2))
    *
    *   formula.pureLiterals // => Set(2, -3)
    *
    * @return
    */
  def pureLiterals: Set[Int] =
    symmetricDifference(extractLiterals(_ < 0), extractLiterals(_ > 0))

  private def extractLiterals(fn: Int => Boolean): Set[Int] =
    clauses.flatMap(_.literals filter fn).toSet

  private def symmetricDifference(a: Set[Int], b: Set[Int]) =
    a.filterNot { lit => b.contains(-lit) } ++
      b.filterNot { lit => a.contains(-lit) }

  override def toString = s"Formula(${clauses.mkString(", ")})"
}
