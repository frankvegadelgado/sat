package frank.problems

import frank.sat.{Clause, Formula}

/**
  * Created by frank on 8/7/2019.
  * Instance of a problem
  */
sealed trait Instance

/**
  * A Boolean formula in 3CNF such that each clause can be false for some truth assignment
  * variables where all the variables must be represented by some number from 1 to n.
  * Answer: Count the sum of weighted function
  * This problem is P.
  */
case class ThreeSat(formula: Formula) extends Instance{
  assert(formula.isExactlyKSat(3), "Should be in 3CNF")
  assert(formula.isVariablesFromOneToN, "Variables should be all from 1 to n")
  assert(formula.freeTautology, "Should not contain tautology clauses")
}


/**
  * Dag Node
  * @param start
  * @param target
  * @param indexClause
  * @param currentCount
  * @param selectedClause
  * @param literal
  */
case class DagNode(start: Boolean,
                   target: Boolean,
                   indexClause: Int,
                   currentCount: Int,
                   selectedClause: Int,
                   literal: Int){
  def next(clauses: Map[Int, Clause], m: Int, n: Int): Seq[DagNode] = {
    this match {
      case DagNode(true, true, -1, 0, -1, 0) =>
        if (m != clauses.size) throw new Exception("Wrong number of clauses")
        else{
          for(j<- 1 to m)
            yield DagNode(true, false, m, 0, j, 0)
        }
      case DagNode(true, false, i, current, selected, lit) if  math.abs(lit) > n=>
        Seq(DagNode(false, current == 3, -1, 0, -1, 0))
      case DagNode(true, false, i, current, selected, lit) if  current > 3=>
        Seq(DagNode(false, false, -1, 0, -1, 0))
      case DagNode(true, false, i, current, selected, lit) if  math.abs(lit) <= n && i >= m =>
        val abs = math.abs(lit)
        Seq(DagNode(true, false, 0, current, selected, abs + 1),
          DagNode(true, false, 0, current, selected, -(abs + 1)))
      case DagNode(true, false, i, current, selected, lit) if  math.abs(lit) <= n && i < m =>
       val newCurrent = if (clauses(i).contain(lit) && (i + 1) == selected) current + 1 else current
        Seq(DagNode(true, false, i + 1, newCurrent, selected, lit))
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
  * The sum of weighted function in some 3SAT formula
  */
case class AnswerCount(value: Double) extends Instance{
  assert(value > 0, "Should be greater than 0")
}


