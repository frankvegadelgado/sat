package frank.problems

import frank.sat.{Clause, Formula}

import scala.collection.immutable.Map

/**
  * Created by frank on 8/7/2019.
  * Instance of a problem
  */
sealed trait Instance




/**
  * A Boolean formula in 2CNF such that each clause can be false for some truth assignment
  * variables where all the variables must be represented by some number from 1 to n.
  */
case class FormulaSat(formula: Formula) extends Instance{
  assert(formula.isExactlyKSat(2), s"The formula should be in 2-CNF")
  assert(formula.isVariablesFromOneToN, "In the formula, the variables should be all from 1 to n")
  assert(formula.freeTautology, "The formula should not contain tautology clauses")
  assert(formula.isMonotoneSat, "The formula should not contain negated variables")

  def max = formula.variables.max
  def count = formula.clauseCount
}


/**
  * Dag Node
  * @param start
  * @param target
  * @param source
  * @param currentCount
  * @param destination
  * @param sum
  */
case class DagNode(start: Boolean,
                   target: Boolean,
                   source: Int,
                   destination: Int,
                   currentCount: Int,
                   sum: Double) {
  def next(clauses: Set[Clause], n: Int, scale: Int, Hm: Double): Seq[DagNode] = {
    this match {
      case DagNode(true, true, -1, 0, -1, 0) =>
        Seq(DagNode(true, false, 1, 0, 0, 0))
      case DagNode(true, false, s, d, c, sm) if  sm > Hm=>
        Seq(DagNode(false, false, -1, 0, -1, 0))
      case DagNode(true, false, s, d, c, sm) if  c == n=>
        if (d == 0 && s == n) {
          val value = sm + BigDecimal(1.0 / s).setScale(scale, BigDecimal.RoundingMode.HALF_UP).toDouble
          Seq(DagNode(false, value == Hm, -1, 0, -1, 0))
        }
        else{
          Seq(DagNode(false, false, -1, 0, -1, 0))
        }
      case DagNode(true, false, s, d, c, sm) if  c < n=>
        if (d == 0 && c == 0){
          for(j<- 0 to n; if s != j)
            yield DagNode(true, false, s, j, 1, 0)
        }
        else if (clauses.contains(Clause(s, d))){
          val value = sm + BigDecimal(1.0/s).setScale(scale, BigDecimal.RoundingMode.HALF_UP).toDouble
          for(j<- 0 to n; if s != j && d != j)
            yield DagNode(true, false, d, j, c + 1, value)
        }
        else{
          Seq(DagNode(false, false, -1, 0, -1, 0))
        }
      }
  }
  def isInitialState = start && target
  def isFinalState = !start
  def isAcceptanceState = !start && target
  def isRejectedState = !start && !target

}

/**
  * A DAG. Ready to make a topological sort
  */
case class GraphDag(nodes: Map[DagNode, Seq[DagNode]]) extends Instance{

}


/**
  * The sum of weighted function in some SAT formula
  */
case class Answer(value: Boolean) extends Instance{
}


