package frank.sat

import scala.util.Random

/**
  * Generate random data.
  */
object Generator {

  /**
    * Generate a collection of random literals.
    *
    * @param count Number of literals to generate
    * @return Random literals
    */
  def generateLiterals(count: Int): Seq[Int] =
    1 to count map randomLiteral

  private def randomLiteral(variable: Int) =
    if (Random.nextBoolean) variable
    else -variable

  /**
    * Generate a random clause.
    *
    * @param sequence Sequence of literals to pull from
    * @param count Number of literals to add to the clause
    * @param contradictions Number of clause literals that will contradict
    *                       literals from the sequence
    * @return Clause
    */
  def generateClause(sequence: Seq[Int], count: Int,
                     contradictions: Int = 0): Clause = {
    val candidates = Stream.continually {
      sequence(Random.nextInt(sequence.length))
    }.take(count)
    val (negative, positive) = candidates.splitAt(contradictions)
    val literals = positive ++ (negative map (-_))
    Clause(Random.shuffle(literals): _*)
  }
}
