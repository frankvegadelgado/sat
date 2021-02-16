package frank.app

/**
  * Created by frank on 2/8/2021.
  */
import frank.sat._
import frank.sat.parsers.DimacsFormula
import java.io.File

import frank.problems.{ReductionComplex, ReductionCount, FormulaSat, AnswerCount}


import scala.io.Source

/**
  * Created by frank on 8/2/2020.
  */
object BatchApp {

  final val endName = ".cnf"

  def printHelp(): Unit = {

    println(s"""|
                |
                |--------------------------------------------------------------------------------------------------
                |                                         This is a Sat Solver
                |                                       Problem: #CLAUSES-KUNSAT
                |                                      Author: Frank Vega Delgado
                |                                       Created: June 19, 2020
                |
                | Begin Help
                |
                |
                |   Provide only two arguments
                |
                |
                |   1st arg  :
                |
                |   --help   :   Print this help documentation
                |
                |
                |   --reduce :   Computed the sum of every unsatisfied clauses
                |                for all truth assignments in formulas MONOTONE-2SAT
                |
                |   2nd arg :
                |
                |           :    A valid directory path, such that the directory contains
                |                the formulas in DIMACS format (with the file extension: '.cnf')
                |
                |   3rd arg :
                |
                |           :    The number of literals per clause in the
                |                the formulas in DIMACS format (with the file extension: '.cnf')
                |
                | End Help
                |--------------------------------------------------------------------------------------------------
                |
                |   """.stripMargin)

  }


  def getListOfFiles(dir: String): List[String] = {
    val file = new File(dir)
    file.listFiles.filter(_.isFile)
      .filter(file => file.getName.endsWith(endName))
      .map(_.getPath).toList
  }

  def readFormula(path: String): Formula = {

    val src = Source.fromFile(path)
    val lines = src.getLines().toSeq

    val result = DimacsFormula.parse(lines)
    result
  }

  def readAllFormulas(dir: String): Seq[(String, Formula)] =
    getListOfFiles(dir).map(fileName => (fileName, readFormula(fileName)))


  def createOutputs(dir: String, inputs: Seq[(String, Formula)], kSat: Int): Seq[String] = {
    for ((path, formula) <- inputs) yield {
      val file = new File(path)
      val fileName = file.getName

      ReductionCount.reduction(ReductionComplex.reduction(FormulaSat(formula, kSat))) match {
        case AnswerCount(value) => s"$fileName: $value"
      }

    }
  }

  def reduce(dir: String, kSat: Int): Unit = {
    println("Starting....")

    println("RUNNING TASK")
    val startTime = System.currentTimeMillis()

    val formulas = readAllFormulas(dir)
    val strings = createOutputs(dir, formulas, kSat)

    val endTime = System.currentTimeMillis()
    println("* EXECUTION TIME IN MILLISECONDS: " + (endTime - startTime))

    strings.foreach(println(_))

    println("Finished....")

  }




  def run(args: Array[String]): Unit = {

    val arguments = if (args.length != 3)
      ("--help", "", "")
    else
      (args(0), args(1), args(2))

    arguments match {
      case (another, _, _)  if another == "--help" || "--reduce" != another => printHelp()
      case ("--reduce", dir, kSat) => reduce(dir, kSat.toInt)

    }

  }


}