package frank.problems

import frank.sat.{Clause, Formula}

import scala.collection.immutable.Map

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
object ReductionComplex extends Reduction[FormulaSat, GraphDag] with ConstantNodes{
  override def reduction(input: FormulaSat): GraphDag = {

    val m = input.formula.clauseCount
    val n = input.formula.variables.max
    val kSat = input.kSAT
    val zippedClauses: Seq[(Clause, Int)]  = input.formula.clauses.zipWithIndex
    val clausesMap: Map[Int, Clause] = zippedClauses.map(t => (t._2, t._1)).toMap
    var nodes: Map[DagNode, Seq[DagNode]] = Map[DagNode, Seq[DagNode]]()

    val next = initial.next(clausesMap, m, n, kSat)

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
            val nextStep = node.next(clausesMap, m, n, kSat)
            nodes = nodes + (node -> nextStep)
            val seq = nextStep.filterNot(nodes.contains(_))
            if (seq.nonEmpty) {
              sequences = sequences :+ seq
            }
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


  def topologicalSort(graph: Map[DagNode, Seq[DagNode]], graphReverse: Map[DagNode, Seq[DagNode]]):  List[DagNode] ={
    var nodes: Map[DagNode, Seq[DagNode]] = graph
    var reverseNodes: Map[DagNode, Seq[DagNode]] = graphReverse
    var L: List[DagNode] = Nil
    var S: Set[DagNode] = Set(initial)

    while (S.nonEmpty) {
      val node = S.head
      S = S.tail
      L = L :+ node
      val edges = nodes(node)
      nodes = nodes - node
      for (m <- edges) {
        reverseNodes = reverseNodes.updated(m, reverseNodes(m).filterNot(_ == node))
        if (reverseNodes(m).isEmpty) {
          S = S + m
        }
      }
    }
    if (nodes.nonEmpty)
      throw new Exception("There is a cycle in DAG")
    else
      L   //a topologically sorted order
  }

  def reverse(graph: Map[DagNode, Seq[DagNode]]): Map[DagNode, Seq[DagNode]] = {
    var result = Map[DagNode, Seq[DagNode]]()
    val keys = graph.keys
    for(node <- keys){
      for(neighbor <- graph(node)){
        if (!result.contains(neighbor)){
          result = result + (neighbor -> Nil)
        }
        result = result.updated(neighbor, result(neighbor) :+ node)
      }
    }
    result
  }

  override def reduction(input: GraphDag): AnswerCount = {
    val nodes = input.nodes
    var keys = nodes.keys.map(node => (node, 0.0)).toMap
    keys = keys.updated(yes, 1)
    val reverseNodes = reverse(nodes)
    val sort = topologicalSort(nodes, reverseNodes)
    val zippedClauses: Seq[(DagNode, Int)]  = sort.reverse.zipWithIndex
    val nodesMap: Map[Int, DagNode] = zippedClauses.map(t => (t._2, t._1)).toMap
    //println(s"$nodesMap")
    val max = nodesMap.size - 1
    for (i <- 0 to max) {
      val node = nodesMap(i)
      val edges = if (node == initial) Seq() else reverseNodes(node)
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
