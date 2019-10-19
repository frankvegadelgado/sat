package frank.sat.parsers

import java.io.{File, PrintWriter}

import frank.sat._

import scala.io.Source

/**
  * Read/write objects in DIMACS format.
  */
trait Dimacs[T] {

  def parse(lines: Seq[String]): T

  def dump(obj: T): Seq[String]

  def write(path: String, obj: T): Unit = {
    val writer = new PrintWriter(new File(path))
    try dump(obj).foreach(writer.println) finally writer.close()
  }

  def read(path: String): T = readSource(Source.fromFile(path))

  def read: T = readSource(Source.stdin)

  private def readSource(source: Source) =
    try parse(source.getLines.toList) finally source.close()

  protected def stripComments(lines: Seq[String]): Seq[String] =
    lines.filterNot (_.startsWith("c"))
}

/**
  * Read/write formulas in DIMACS format.
  */
object DimacsFormula extends Dimacs[Formula] {

  private[this] val Header = "p cnf (\\d+) (\\d+)".r

  def parse(lines: Seq[String]): Formula =
    stripComments(lines) match {
      case Header(_, _) +: clauses =>
        Formula(clauses map { clause =>
          Clause(clause.split(" ").init map (_.toInt): _*)
        }: _*)
      case _ => throw new IllegalArgumentException
    }

  def dump(formula: Formula): Seq[String] = {
    val header = s"p cnf ${formula.variableCount} ${formula.clauseCount}"
    val clauses = formula.clauses.map {
      clause => s"${clause.literals.mkString(" ")} 0"
    }
    header +: clauses
  }
}

/**
  * Read/write results in DIMACS format.
  */
object DimacsResult extends Dimacs[Result] {

  val Header = "s (UNKNOWN|UNSATISFIABLE|SATISFIABLE)".r
  val Literals = "v (.+) 0".r

  def parse(lines: Seq[String]): Result =
    stripComments(lines) match {
      case Header("UNKNOWN") :: Nil => Unknown
      case Header("UNSATISFIABLE") :: Nil => Unsatisfiable
      case Header("SATISFIABLE") :: Nil => Satisfiable()
      case Header("SATISFIABLE") :: Literals(literals) :: Nil =>
        Satisfiable(literals.split(" ") map (_.toInt): _*)
      case _ => throw new IllegalArgumentException
    }

  def dump(result: Result): Seq[String] =
    result match {
      case Unknown => Seq("s UNKNOWN")
      case Unsatisfiable => Seq("s UNSATISFIABLE")
      case Satisfiable() => Seq("s SATISFIABLE")
      case Satisfiable(literals @ _*) =>
        Seq("s SATISFIABLE", s"v ${literals.mkString(" ")} 0")
    }
}
