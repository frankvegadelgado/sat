package frank.problems

import org.scalatest.FlatSpec
import frank.sat._
/**
  * Created by frank on 8/7/2019.
  */
class ReductionCount$Test extends FlatSpec with Common {
  //private val reducerComplex = ReductionComplexity
  //private val reducerCount = ReductionCount
  private val reducer = ReductionComplexity
  behavior of "ReductionCount$"

  val formula1 = Formula(Clause(1, 2))
  val formula2 = Formula(Clause(1, 2), Clause(3, 4))
  val formula3 = Formula(Clause(1, 2), Clause(3, 2), Clause(2, 3))
  val formula4 = Formula(Clause(1, 2), Clause(3, 2))
  val formula5 = Formula(Clause(2, 1), Clause(1, 3))
  /*
  it should "reduce CNF formulas to DAG graph" in {
    reduceComplex(reducerComplex, formula1)
    reduceComplex(reducerComplex, formula2)
    reduceComplex(reducerComplex, formula3)
    reduceComplex(reducerComplex, formula4)

  }
  */
  it should "decide whether directed graph has a Hamiltonian path" in {

    reduce(reducer, formula1, true)
    reduce(reducer, formula2, false)
    reduce(reducer, formula3, true)
    reduce(reducer, formula4, false)
    reduce(reducer, formula5, false)
  }



}
