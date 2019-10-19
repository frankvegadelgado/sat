package frank.problems

import frank.sat.Formula

/**
  * Created by frank on 8/7/2019.
  * Instance of a problem
  */
sealed trait Instance

/**
  * A Boolean formula in 3CNF such that each clause has no negation
  * variables.
  * Question: Is there a truth assignment for the formula such that each clause has at least one true
  * literal and at least one false literal?
  * This problem is a known NP-complete language.
  */
case class MonotoneNaeSat(formula: Formula) extends Instance{
  assert(formula.isExactlyKSat(3), "Should be in 3CNF")
  assert(formula.isMonotoneSat, "Should be Monotone")
}

/**
  * A positive integer K and a Boolean formula that is an instance of
  * XOR 2SAT such that each clause has no negation variables.
  * Question: Is there a truth assignment in the formula such that at most K clauses are unsatisfiable?
  * This problem is in NP-complete.
  */
case class MinXor2Sat(K: Int, formula: Formula) extends Instance{
  assert(formula.isExactlyKSat(2), "Should be in 2CNF")
  assert(formula.isMonotoneSat, "Should be Monotone")
  assert(K > 0, "K should be a positive integer")
  assert(K <= formula.variableCount, "K should be at most all the clauses")

}


/**
  * An instance of XOR 2SAT such that each clause has no negation variables.
  * Question: Is there a satisfying truth assignment for the Boolean formula?
  * This problem is in NP-complete.
  */
case class MonotoneXor2Sat(formula: Formula) extends Instance{
  assert(formula.isExactlyKSat(2), "Should be in 2CNF")
  assert(formula.isMonotoneSat, "Should be Monotone")
}


