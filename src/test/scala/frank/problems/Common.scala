package frank.problems

/**
  * Created by frank on 8/7/2019.
  */

import frank.sat.{Formula, Satisfiable}
import frank.sat.solvers.XORSolver
import org.scalatest.Matchers
import org.scalatest.Inside._

trait Common extends Matchers {

  def reduceComplex(reducer: Reduction[FormulaSat, GraphDag], input: Formula, kSat: Int) = {
    reducer.reduction(FormulaSat(input, kSat)) should matchPattern {
      case output:GraphDag =>
    }
  }

  def reduceCount(reducerComplex: Reduction[FormulaSat, GraphDag], reducerCount: Reduction[GraphDag, AnswerCount], input: Formula, left: Double, right: Double, kSat: Int) = {
    val value = reducerCount.reduction(reducerComplex.reduction(FormulaSat(input, kSat)))
    value should matchPattern {
      case output: AnswerCount =>
    }
    inside(value) {
      case output: AnswerCount => output should matchPattern {
        case AnswerCount(value) if left <= value && value <= right =>
      }
    }
  }
}
