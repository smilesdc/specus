package net.tomasherman.specus.server.plugin.definitions

import util.parsing.combinator.syntactical.StandardTokenParsers
import net.tomasherman.specus.server.api.plugin.definitions.{Interval, EqGt, PluginVersionConstraint, VersionConstraintParser}
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



object ParserCombinatorsVersionConstraintParser extends StandardTokenParsers with VersionConstraintParser{

  trait ParsingResult
  case class OK(const:PluginVersionConstraint)
  case class Fail(error:String)

  lexical.reserved += ("_")
  lexical.delimiters += (".",">=","(",")",",")

  def constraint: Parser[PluginVersionConstraint] =
    ( gteqConstraint | intervalConstraint ) ^^ {case x => x}
   
  def gteqConstraint: Parser[PluginVersionConstraint] =
    (">=" ~ major) ^^ {case ">=" ~ x => new EqGt(new MajorMinorBuildPluginVersion(x._1,x._2,x._3))}

  def intervalConstraint: Parser[PluginVersionConstraint] =
    ("(" ~ major ~ "," ~ major ~ ")") ^^ {
      case "(" ~ x ~ "," ~ y ~ ")" => new Interval(
        (new MajorMinorBuildPluginVersion(x._1,x._2,x._3),
        new MajorMinorBuildPluginVersion(y._1,y._2,y._3)))
    }

  def major: Parser[(Option[Int],Option[Int],Option[Int])] =
    (numericLit ~ "." ~ minor) ^^ {
      case x ~ "." ~ y => (Some(x.toInt),y._1,y._2)
    }

  def minor: Parser[(Option[Int],Option[Int])] =
    ((numericLit | "_") ~ "." ~ build) ^^ {
      case "_" ~ "." ~ None => (None,None)
      case x ~ "." ~ build => (Some(x.toInt),build)
    }

  def build: Parser[Option[Int]] =
    (numericLit | "_") ^^ {
      case "_" => None
      case x => Some(x.toInt)
    }

  def parse(data: String) = constraint(new lexical.Scanner(data)) match {
    case Success(const, _) => OK(const)// ord is a ClientOrder
    case Failure(msg, _) => Fail(msg)
    case Error(msg, _) => Fail(msg)
  }
}