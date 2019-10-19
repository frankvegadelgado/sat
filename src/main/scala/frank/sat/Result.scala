package frank.sat

sealed trait Result

/**
  * The formula is satisfiable.
  *
  * @param literals Literals that solve the formula
  */
case class Satisfiable(literals: Int*) extends Result

/**
  * The formula is unsatisfiable.
  */
case object Unsatisfiable extends Result

/**
  * The satisfiability of the formula cannot be determined.
  */
case object Unknown extends Result
