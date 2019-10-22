package frank.sat.solvers

import frank.sat._

import scala.util.control.Breaks


/**
  * Solve HORN-SAT CNF formulas
  *
  * The algorithm has polynomial running time.
  */
object HORNSolver extends Solver {

  /**
    * Solve a formula.
    *
    * @param formula Formula to solve
    * @return Result
    */
  override def solve(formula: Formula): Result =
    solverImpl(Step(formula))

  protected def solverImpl(input: Step): Result = {
    require(input.formula.isExactlyKPosVar(1), "formula is not exactly HORN-SAT")

    val solution = solveHornSat(input.formula)

    solution match {
      case Some(_) => Satisfiable()
      case None => Unsatisfiable
    }

  }

  def solveHornSat(formula: Formula):Option[Set[Int]] = {
    // build the data structure
    var pool = Map[Int, Set[Clause]]()
    var clausesWithNegVar = Map[Int, Set[Clause]]()
    var score = Map[Clause, Int]()

    for(c <- formula.clauses){
      val negVars = c.negVarCount
      score = score ++ Map((c, negVars))
      if (pool.contains(negVars)){
        pool = pool.updated(negVars, pool(negVars) ++ Set(c))
      } else {
        pool = pool ++ Map((negVars, Set(c)))
      }
    }

    for (v <- formula.variables){
      for(c <- formula.clauses){
        if (c.contain(-v)){
          if (clausesWithNegVar.contains(v)){
            clausesWithNegVar = clausesWithNegVar.updated(v, clausesWithNegVar(v) ++ Set(c))
          } else {
            clausesWithNegVar = clausesWithNegVar ++ Map((v, Set(c)))
          }
        }
      }
    }
    //  --- solve Horn SAT formula
    var solution:Option[Set[Int]] = Some(Set[Int]()) //contains all variables set to True
    val loop = new Breaks
    loop.breakable {
      while (pool.contains(0) && !pool(0).isEmpty) {
        val curr = pool(0).head // arbitrary zero score clause
        pool = pool.updated(0, pool(0) diff Set(curr))
        val vOption = curr.posVar
        if (vOption.isEmpty) //formula is not satisfiable
        {
          solution = None
          loop.break
        }
        val v = vOption.get
        if (!solution.contains(v) && !clausesWithNegVar(v).contains(curr)) {
          val set: Set[Int] = solution.get ++ Set(v)
          solution = Some(set)
          for (c <- clausesWithNegVar(v)) //update score
          {
            pool = pool.updated(score(c), pool(score(c)) diff Set(c))
            score = score.updated(c, score(c) - 1)
            if (pool.contains(score(c)))
              pool = pool.updated(score(c), pool(score(c)) ++ Set(c)) //change c to lower score in pool
            else
              pool = pool ++ Map((score(c), Set(c)))
          }
        }
      }
    }

    solution
  }


}
