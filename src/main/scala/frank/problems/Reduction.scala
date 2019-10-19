package frank.problems

import frank.sat.{Clause, Formula}

/**
  * Created by frank on 8/7/2019.
  * The reduction of a problem into another one using a certificate
  */
trait Reduction[U<:Instance, V<:Instance, C] {
  def reduction(input: U, certificate: Option[C] = None): V
}

/**
  * Theorem 6 in paper https://www.preprints.org/manuscript/201908.0037/v1
  */
object ReductionKnownNPComplete extends Reduction[MonotoneNaeSat, MinXor2Sat, Any]{
  override def reduction(input: MonotoneNaeSat, certificate: Option[Any] = None): MinXor2Sat = {

    val clauses: Seq[List[Clause]] = for {(clause, index) <- input.formula.clauses.zipWithIndex
                                     variable = input.formula.variables.max + 3*index} yield {
      List(Clause(variable + 1, variable + 2),
        Clause(variable + 1, variable + 3),
        Clause(variable + 2, variable + 3),
        Clause(variable + 1, clause.literals.zipWithIndex.find(_._2 == 0).get._1),
        Clause(variable + 2, clause.literals.zipWithIndex.find(_._2 == 1).get._1),
        Clause(variable + 3, clause.literals.zipWithIndex.find(_._2 == 2).get._1))

    }
    MinXor2Sat(input.formula.clauseCount, Formula(clauses.flatten: _*))
  }
}

/**
  * Theorem 7 in paper https://www.preprints.org/manuscript/201908.0037/v1
  */
object ReductionWithVerification extends Reduction[MinXor2Sat, MonotoneXor2Sat, Array[Int]]{
  override def reduction(input: MinXor2Sat, certificate: Option[Array[Int]] = None): MonotoneXor2Sat = {
    if (certificate.isEmpty) throw new IllegalArgumentException("The certificate is not valid")
    val array: Array[Int] = certificate.get
    // Output the value of m
    val m = input.formula.clauseCount
    val indexed = input.formula.clauses.zipWithIndex
    var clauses: List[Clause] = Nil
    var min = 1
    var max = 0
    for(i <- 1 to input.K + 1){
      if (i == input.K + 1){
        if (array.size >  input.K) throw new IllegalArgumentException("The certificate is not valid")
        max = m + 1
      } else if (array.size < i || array(i - 1) <= max || array(i - 1) < 1 || array(i - 1) > m ) {
        throw new IllegalArgumentException("The certificate is not valid")
      } else {
        max = array(i - 1)
      }
      for(j <- min to (max - 1)){
        val c_j = indexed.find(p => p._2 == j - 1).get._1
        clauses =  c_j::clauses
      }
      min = max + 1
    }
    MonotoneXor2Sat(Formula(clauses: _*))
  }
}