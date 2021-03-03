package frank.problems

/**
  * Created by frank on 8/7/2019.
  */

import frank.sat.{Formula, Satisfiable}
import frank.sat.solvers.XORSolver
import org.scalatest.Matchers
import org.scalatest.Inside._

trait Common extends Matchers {

  def reduceComplex(reducer: Reduction[FormulaSat, GraphDag], input: Formula) = {
    reducer.reduction(FormulaSat(input)) should matchPattern {
      case output:GraphDag =>
    }
  }

  def reduceCount(reducerComplex: Reduction[FormulaSat, GraphDag], reducerCount: Reduction[GraphDag, Answer], input: Formula, respond: Boolean) = {
    val value = reducerCount.reduction(reducerComplex.reduction(FormulaSat(input)))
    value should matchPattern {
      case output: Answer =>
    }
    inside(value) {
      case output: Answer => output should matchPattern {
        case Answer(value) if respond == value =>
      }
    }
  }

  def reduce(reducer: Reduction[FormulaSat, Answer], input: Formula, respond: Boolean) = {
    val value = reducer.reduction(FormulaSat(input))
    value should matchPattern {
      case output: Answer =>
    }
    inside(value) {
      case output: Answer => output should matchPattern {
        case Answer(value) if respond == value =>
      }
    }
  }
}
