package net.tomasherman.specus.server.plugin.definitions

import util.parsing.combinator.syntactical.StandardTokenParsers
import net.tomasherman.specus.server.api.plugin.definitions._

/**
 * This file is part of Specus.
 *
 * Specus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Specus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Specus.  If not, see <http://www.gnu.org/licenses/>.
 *
 */



object ParserCombinatorsVersionConstraintParser extends StandardTokenParsers
  with MajorMinorBuildVersionPluginParser{

  trait ParsingResult
  case class IntervalParsed(bounds: (PluginVersion,PluginVersion)) extends ParsingResult
  case class EqGtParsed(value: PluginVersion) extends ParsingResult
  case class Fail(error: String) extends ParsingResult

  lexical.delimiters += (">=","(",")",",")

  def constraint: Parser[ParsingResult] =
    ( eqgtConstraint | intervalConstraint ) ^^ {case x => x}
   
  def eqgtConstraint: Parser[EqGtParsed] =
    (">=" ~ version) ^^ {case ">=" ~ x => new EqGtParsed(new MajorMinorBuildPluginVersion(x._1,x._2,x._3))}

  def intervalConstraint: Parser[IntervalParsed] =
    ("(" ~ version ~ "," ~ version ~ ")") ^^ {
      case "(" ~ x ~ "," ~ y ~ ")" => new IntervalParsed((
        new MajorMinorBuildPluginVersion(x._1,x._2,x._3),
        new MajorMinorBuildPluginVersion(y._1,y._2,y._3)))
    }


  def parse(data: String) = constraint(new lexical.Scanner(data)) match {
    case Success(parsed, _) =>  parsed
    case Failure(msg, _) => Fail(msg)
    case Error(msg, _) => Fail(msg)
  }
}