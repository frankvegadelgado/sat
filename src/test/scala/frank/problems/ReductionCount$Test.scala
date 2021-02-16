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
  val formula4 = Formula(Clause(1, 2), Clause(2, 3), Clause(1, 3))

  it should "reduce CNF formulas to DAG graph" in {
    reduceComplex(reducerComplex, formula1, 2)
    reduceComplex(reducerComplex, formula2, 2)
    reduceComplex(reducerComplex, formula3, 2)
    reduceComplex(reducerComplex, formula4, 2)

  }

  it should "reduce CNF formulas to sum of weighted function" in {

    reduceCount(reducerComplex, reducerCount, formula1, 1, 1, 2)
    reduceCount(reducerComplex, reducerCount, formula2, 8, 8, 2)
    reduceCount(reducerComplex, reducerCount, formula3, 4, 4, 2)
    reduceCount(reducerComplex, reducerCount, formula4, 6, 6, 2)
  }



}
