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
object ReductionComplexity extends Reduction[FormulaSat, Answer] with ConstantNodes{
  override def reduction(input: FormulaSat): Answer = {

    val n = input.formula.variables.max
    val clauses: Set[Clause] = input.formula.clauses.toSet
    val scale = (math.floor(math.log(n + 1)/math.log(2)) + 1).toInt
    var Hm: Double = 0
    for(j<- 1 to n){
      Hm = Hm + BigDecimal(1.0/j).setScale(scale, BigDecimal.RoundingMode.HALF_UP).toDouble
    }

    var nodes: Map[DagNode, Seq[DagNode]] = Map[DagNode, Seq[DagNode]]()


    val next = initial.next(clauses, n, scale, Hm)

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
            val nextStep = node.next(clauses, n, scale, Hm)
            nodes = nodes + (node -> nextStep)
            val seq = nextStep.filterNot(nodes.contains(_))
            if (seq.nonEmpty) {
              sequences = sequences :+ seq
            }
          }
        }
    }
    if (!nodes.contains(yes)){
      Answer(false)
    } else {
      Answer(true)
    }
    //GraphDag(nodes)
  }
}

/**
  *
  */
/*
object ReductionCount extends Reduction[GraphDag, AnswerCount] with ConstantNodes{

  override def reduction(input: GraphDag): AnswerCount = {
    val graph = input.nodes
    val nodes = graph.keys.zipWithIndex.toMap

    if (!nodes.contains(yes)){
      AnswerCount(false)
    } else {
      AnswerCount(true)
    /*
      val len = nodes.size
      val dist = Array.ofDim[Boolean](len, len)
      for (node <- nodes) {
        for (neighbor <- graph(node._1)) {
          val index = nodes(neighbor)
          dist(node._2)(index) = true
          dist(index)(node._2) = true
        }
      }

      for (k <- 1 to len) {
        for (i <- 1 to len) {
          for (j <- 1 to len) {
            if (!dist(i - 1)(j - 1) && dist(i - 1)(k - 1) && dist(k - 1)(j - 1)) {
              dist(i - 1)(j - 1) = true
            }
          }
        }
      }

      //println(s"nodes")
      val answer = dist(nodes(initial))(nodes(yes))
      //println(s"answer: ${answer}")
      AnswerCount(answer)*/
    }
  }
}
*/