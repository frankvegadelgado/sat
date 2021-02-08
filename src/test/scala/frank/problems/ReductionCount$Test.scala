package frank.problems

import org.scalatest.FlatSpec
import frank.sat._
/**
  * Created by frank on 8/7/2019.
  */
class ReductionCount$Test extends FlatSpec with Common {
  private val reducerComplex = ReductionComplex
  private val reducerCount = ReductionCount
  behavior of "ReductionCount$"

  val formula1 = Formula(Clause(1, 2))
  val formula2 = Formula(Clause(1, 2), Clause(3, 4))
  val formula3 = Formula(Clause(1, 2), Clause(2, 3))
  val formula4 = Formula(Clause(-1, -2), Clause(-1, 2), Clause(1, -2), Clause(2, 3))
  val formula5 = Formula(Clause(-1, -2), Clause(-1, 2), Clause(1, -2),
                         Clause(-2, -3), Clause(-2, 3), Clause(2, -3))

  it should "reduce CNF formulas to DAG graph" in {
    reduceComplex(reducerComplex, formula1)
    reduceComplex(reducerComplex, formula2)
    reduceComplex(reducerComplex, formula3)
    reduceComplex(reducerComplex, formula4)
    reduceComplex(reducerComplex, formula5)

  }

  it should "reduce CNF formulas to sum of weighted function" in {

    reduceCount(reducerComplex, reducerCount, formula1, 1, 1)
    reduceCount(reducerComplex, reducerCount, formula2, 8, 8)
    reduceCount(reducerComplex, reducerCount, formula3, 4, 4)
    reduceCount(reducerComplex, reducerCount, formula4, 8, 8)
    reduceCount(reducerComplex, reducerCount, formula5, 12, 12)
  }



}
