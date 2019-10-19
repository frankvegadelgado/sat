package frank.problems

/**
  * Created by frank on 8/7/2019.
  */

import frank.sat.{Formula, Satisfiable}
import frank.sat.solvers.XORSolver
import org.scalatest.Matchers
import org.scalatest.Inside._

trait Common extends Matchers {

  def reduceSat(reducer: Reduction[MonotoneNaeSat, MinXor2Sat, Any], input: Formula) = {
    reducer.reduction(MonotoneNaeSat(input)) should matchPattern {
      case output:MinXor2Sat =>
    }
  }

  def verifySat(verifier: Reduction[MinXor2Sat, MonotoneXor2Sat, Array[Int]], reducer: Reduction[MonotoneNaeSat, MinXor2Sat, Any], input: Formula, certificate: Array[Int]) = {
    val value = verifier.reduction(reducer.reduction(MonotoneNaeSat(input)), Some(certificate))
    value should matchPattern {
      case output: MonotoneXor2Sat =>
    }
    inside(value) {
      case output: MonotoneXor2Sat => XORSolver.solve(output.formula) should matchPattern {
        case Satisfiable() =>
        case Satisfiable(literals@_*) if !literals.isEmpty =>
      }
    }
  }
}
