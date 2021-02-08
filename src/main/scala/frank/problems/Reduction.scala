package frank.problems

import frank.sat.{Clause, Formula}

/**
  * Created by frank on 8/7/2019.
  * The reduction of a problem into another one using a certificate
  */
trait Reduction[U<:Instance, V<:Instance] {
  def reduction(input: U): V
}

trait ConstantNodes{
  val initial = DagNode(true, true, -1, 0, -1, 0)
  val no = DagNode(false, false, -1, 0, -1, 0)
  val yes = DagNode(false, true, -1, 0, -1, 0)


}

/**
  *
  */
object ReductionComplex extends Reduction[ThreeSat, GraphDag] with ConstantNodes{
  override def reduction(input: ThreeSat): GraphDag = {

    val m = input.formula.clauseCount
    val n = input.formula.variables.max
    val zippedClauses: Seq[(Clause, Int)]  = input.formula.clauses.zipWithIndex
    val clausesMap: Map[Int, Clause] = zippedClauses.map(t => (t._2, t._1)).toMap
    var nodes: Map[DagNode, Seq[DagNode]] = Map[DagNode, Seq[DagNode]]()

    val next = initial.next(clausesMap, m, n)

    var sequences: List[Seq[DagNode]] = List(next)

    nodes = nodes + (initial -> next)

    while(sequences.nonEmpty){
      val current = sequences.head
      sequences = sequences.tail
      for(node <- current){
        if (node.isFinalState){
          if (!nodes.contains(node)) {
            nodes = nodes + (node -> Seq())
          }
        } else if (!nodes.contains(node)) {
            val nextStep = node.next(clausesMap, m, n)
            nodes = nodes + (node -> nextStep)
            sequences = sequences :+ nextStep
          }
        }
    }
    GraphDag(nodes)
  }
}

/**
  *
  */
object ReductionCount extends Reduction[GraphDag, AnswerCount] with ConstantNodes{

  def hasNoIncomingEdge(node: DagNode, nodes: Map[DagNode, Seq[DagNode]]) =
    nodes.values forall (!_.contains(node))

  def topologicalSort(graph: Map[DagNode, Seq[DagNode]]):  List[DagNode] ={
    var nodes: Map[DagNode, Seq[DagNode]] = graph.map(t => t)
    var L: List[DagNode] = Nil
    var S: Set[DagNode] = Set(initial)

    while (S.nonEmpty) {
      val node = S.head
      S = S.tail
      L = L :+ node
      val edges = nodes(node)
      nodes = nodes - node
      for (m <- edges) {
        if (hasNoIncomingEdge(m, nodes)) {
          S = S + m
        }
      }
    }
    if (nodes.nonEmpty)
      throw new Exception("There is a cycle in DAG")
    else
      L   //a topologically sorted order
  }

  override def reduction(input: GraphDag): AnswerCount = {
    val nodes = input.nodes
    var keys = nodes.keys.map(node => (node, 0.0)).toMap
    keys = keys.updated(yes, 1)
    val sort = topologicalSort(nodes)
    val zippedClauses: Seq[(DagNode, Int)]  = sort.reverse.zipWithIndex
    val nodesMap: Map[Int, DagNode] = zippedClauses.map(t => (t._2, t._1)).toMap
    //println(s"$nodesMap")
    val max = nodesMap.size - 1
    for (i <- 0 to max) {
      val node = nodesMap(i)
      val edges = nodes.filter(t => t._2.contains(node)).keys
      //println(s"$node  and  $edges")
      for (m <- edges) {
        if (keys(node) > 0) {
          keys = keys.updated(m, keys(m) + keys(node))
        }
      }
    }
    //println(s"$keys")
    val answer = keys(initial)/2.0
    //println(s"answer: ${answer}")
    AnswerCount(answer)
  }
}