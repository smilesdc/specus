package net.tomasherman.specus.server.plugin.definitions

import org.specs2.mutable.Specification
import net.tomasherman.specus.server.plugin.definitions.ParserCombinatorsVersionConstraintParser.{IntervalParsed, EqGtParsed, Fail}

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
class ParserCombinatorsVersionConstraintParserSpec extends Specification {
  "ParserCombinatorsVesionConstraintParser" should {
    "parse correctly EqGt" in {
      p(">=1.11.111") must_== EqGtParsed(v(1,11,111))
      p(">=1.1._") must_== EqGtParsed(v(1,1))
      p(">=1._._") must_== EqGtParsed(v(1))
    }

    "fail properly EqGt" in {
      p(">=") must beAnInstanceOf[Fail]
      p(">=_._._") must beAnInstanceOf[Fail]
      p(">=_._._1") must beAnInstanceOf[Fail]
      p(">=_._.1") must beAnInstanceOf[Fail]
      p(">=_.1._") must beAnInstanceOf[Fail]
      p(">=roflmao") must beAnInstanceOf[Fail]
    }

    "parse correctly Interval" in {
      val x = p("(1.11.111,2.1.1)") must_== IntervalParsed((v(1,11,111),v(2,1,1)))
      p("(1._._,2.1.1)") must_== IntervalParsed((v(1),v(2,1,1)))
      p("(1._._,2._._)") must_== IntervalParsed((v(1),v(2)))
      p("(1.11._,2.1.1)") must_== IntervalParsed((v(1,11),v(2,1,1)))
      p("(1.11.111,2.1._)") must_== IntervalParsed((v(1,11,111),v(2,1)))
      p("(1._._,2.1._)") must_== IntervalParsed((v(1),v(2,1)))
     }

    "fail properly Interval" in {
      p("(1.1.1,)") must beAnInstanceOf[Fail]
      p("(1.1.1,_._._)") must beAnInstanceOf[Fail]
      p("(,)") must beAnInstanceOf[Fail]
      p("(,1.1_)") must beAnInstanceOf[Fail]
      p("(1.1.1.1,)") must beAnInstanceOf[Fail]
      p("(1.1.1,1.1.1.1)") must beAnInstanceOf[Fail]
      p("(1.1.1.1,1.1.1.1)") must beAnInstanceOf[Fail]
    }
  }

  def v(i1:Int,i2:Int,i3:Int) = new MajorMinorBuildPluginVersion(i1,i2,i3)
  def v(i1:Int,i2:Int) = new MajorMinorBuildPluginVersion(i1,i2)
  def v(i1:Int) = new MajorMinorBuildPluginVersion(i1)
  def p(str:String) = ParserCombinatorsVersionConstraintParser.parse(str)
}