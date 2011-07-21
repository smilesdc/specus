package net.tomasherman.specus.server.plugin.definitions

import util.parsing.combinator.syntactical.StandardTokenParsers

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
trait MajorMinorBuildVersionPluginParser extends StandardTokenParsers{
  lexical.reserved += ("_")
  lexical.delimiters += (".")

  def version: Parser[(Option[Int],Option[Int],Option[Int])] =
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
}