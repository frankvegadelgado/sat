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

  val formula1 = Formula(Clause(1, 2, 3))
  val formula2 = Formula(Clause(1, 2, 3), Clause(4, 5, 6))
  val formula3 = Formula(Clause(-1, 2, 3), Clause(-1, -2, 3), Clause(-1, 2, -3), Clause(-1, -2, -3),
    Clause(1, -2, 3), Clause(1, 2, -3), Clause(1, -2, -3), Clause(1, 2, 3))
  val formula4 = Formula(Clause(1, 2, 3), Clause(4, 5, 6), Clause(-1, -2, -3), Clause(-4, -5, -6))

  it should "reduce 3CNF formulas to DAG graph" in {
    reduceComplex(reducerComplex, formula1)
    reduceComplex(reducerComplex, formula2)
    reduceComplex(reducerComplex, formula3)
    reduceComplex(reducerComplex, formula4)


  }

  it should "reduce 3CNF formulas to sum of weighted function" in {

    reduceCount(reducerComplex, reducerCount, formula1, 1, 1)
    reduceCount(reducerComplex, reducerCount, formula2, 16, 16)
    reduceCount(reducerComplex, reducerCount, formula3, 8, 8)
    reduceCount(reducerComplex, reducerCount, formula4, 32, 32)

  }
}
