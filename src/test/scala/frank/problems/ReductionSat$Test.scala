package frank.problems

import org.scalatest.FlatSpec
import frank.sat._
/**
  * Created by frank on 8/7/2019.
  */
class ReductionSat$Test extends FlatSpec with Common {
  private val reducer = ReductionKnownNPComplete
  private val verifier = ReductionWithVerification
  behavior of "ReductionSat$"

  val formula1 = Formula(Clause(1, 2, 3))
  val formula2 = Formula(Clause(1, 2, 3), Clause(2, 5, 6))
  val formula3 = Formula(Clause(1, 2, 3), Clause(2, 5, 6), Clause(1, 3, 4))
  val formula4 = Formula(Clause(1, 2, 3), Clause(2, 5, 6), Clause(1, 3, 5))

  it should "reduce 3CNF formulas" in {
    reduceSat(reducer, formula1)
    reduceSat(reducer, formula2)
    reduceSat(reducer, formula3)
    reduceSat(reducer, formula4)


  }

  it should "verify 3CNF formulas" in {

    verifySat(verifier, reducer, formula3, Array(1, 9, 15))
    verifySat(verifier, reducer, formula4, Array(3, 8, 14))


  }
}
