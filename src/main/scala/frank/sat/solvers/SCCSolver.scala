package frank.sat.solvers

import frank.sat._
import java.util
import scala.util.control._
/**
  * Solve 2-SAT CNF formulas by calculating strongly connected
  * components of an implication graph.
  *
  * The algorithm has polynomial running time.
  */
object SCCSolver extends Solver {

  protected def solverImpl(input: Step): Result = {
    require(input.formula.isExactlyKSat(2), "formula is not exactly 2-SAT")

    val graph = adjacencyList(implications(input.formula))
    val scc = tarjanSCC(graph)

    if (!isValid(scc)) Unsatisfiable
    else Satisfiable()
  }

  private def implications(formula: Formula) =
    formula.clauses flatMap { case Clause(a, b) => Seq(-a -> b, -b -> a) }

  private def adjacencyList(implications: Seq[(Int, Int)]) =
    implications.foldLeft(Map.empty[Int, Seq[Int]]) {
      case (vertices, (tail, head)) =>
        val edges = vertices.getOrElse(tail, Seq.empty)
        vertices.updated(tail, edges :+ head)
    }

  // Strong component algorithm iterative
  private def tarjanSCC(graph: Map[Int, Seq[Int]]): Seq[Set[Int]] = {
    var index = 0
    val elements = graph.keys.toSet.union(graph.values.flatten.toSet).toArray
    val map = elements.zipWithIndex.toMap
    val vIndex = Vector.fill(elements.size)(None).toArray[Option[Int]]
    val vLowLink = Vector.fill(elements.size)(Int.MaxValue).toArray
    val S = new util.Stack[Int]()
    var result = Seq[Set[Int]]()
    def strongConnect(vInitial: Int): Unit = {
      val work = new util.Stack[(Int, Int)]()
      val loop = new Breaks
      work.push((vInitial, 0))
      while (work.size > 0) {
        var (v, i) = work.pop()
        if (i == 0) {
          // Set the depth index for v to the smallest unused index
          vIndex(map(v)) = Some(index)
          vLowLink(map(v)) = index
          index = index + 1
          S.push(v)
        }
        var recurse = false
        if (graph.contains(v)) {
          val adjacentMap = graph(v).zipWithIndex.toMap
          // Consider successors of v
          loop.breakable {
            for (w <- graph(v)) {
              if (!vIndex(map(w)).isDefined) {
                // CHANGED: Add w to recursion stack.
                work.push((v, adjacentMap(w) + 1))
                work.push((w, 0))
                recurse = true
                loop.break
              }
              else if (S.contains(w)) {
                vLowLink(map(v)) = math.min(vLowLink(map(v)), vIndex(map(w)).get)
              }
            }
          }
        }
        if (!recurse) {
          // If v is a root node, pop the stack and generate an SCC
          if (vLowLink(map(v)) == vIndex(map(v)).get) {
            var scc = Set[Int]()
            var w = -1
            do {
              w = S.pop()
              scc = scc ++ Set(w)
            } while (w != v)
            result = result ++ Seq(scc)
          }
          if (work.size > 0) {
            val w = v
            val (z, _) = work.peek()
            v = z
            vLowLink(map(v)) = math.min(vLowLink(map(v)), vLowLink(map(w)))
          }
        }
      }
    }

    for {v <- elements if (!vIndex(map(v)).isDefined)} yield strongConnect(v)
    result
  }

  private def isValid(solution: Seq[Set[Int]]) =
    solution.forall(scc => scc forall {literal => !scc.contains(-literal)})


}
