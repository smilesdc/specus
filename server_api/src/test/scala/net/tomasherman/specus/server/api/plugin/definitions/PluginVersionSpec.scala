package net.tomasherman.specus.server.api.plugin.definitions

import org.specs2.mutable.Specification

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


class ImplPV(val v:Int) extends PluginVersion{
  def canCompare(other: PluginVersion) = {
    other match {
      case x:ImplPV => true
      case _ => false
    }
  }

  protected def compare(other: PluginVersion) = {
    other match {
      case other: ImplPV => Some(this.v.compare(other.v))
      case _ => None
    }
  }
}

class PluginVersionSpec extends Specification {
  val v1 = new ImplPV(1)
  val v2 = new ImplPV(2)
  val v3 = new ImplPV(3)
  implicit def optToBoolean(o:Option[Boolean]) = o.get
  "PluginVersion" should {
    ">" in {
      (v1 > v2).get must_== false
      (v1 > v3).get must_== false
      (v1 > v1).get must_== false
      (v3 > v2).get must_== true
      (v3 > v1).get must_== true
      (v3 > v3).get must_== false
      (v2 > v1).get must_== true
      (v2 > v3).get must_== false
      (v2 > v2).get must_== false
    }
    ">=" in {
      (v1 >= v2).get must_== false
      (v1 >= v3).get must_== false
      (v1 >= v1).get must_== true
      (v3 >= v2).get must_== true
      (v3 >= v1).get must_== true
      (v3 >= v3).get must_== true
      (v2 >= v1).get must_== true
      (v2 >= v3).get must_== false
      (v2 >= v2).get must_== true
    }
    "<" in {
      (v1 < v2).get must_== true
      (v1 < v3).get must_== true
      (v1 < v1).get must_== false
      (v3 < v2).get must_== false
      (v3 < v1).get must_== false
      (v3 < v3).get must_== false
      (v2 < v1).get must_== false
      (v2 < v3).get must_== true
      (v2 < v2).get must_== false
    }
    "<=" in {
      (v1 <= v2).get must_== true
      (v1 <= v3).get must_== true
      (v1 <= v1).get must_== true
      (v3 <= v2).get must_== false
      (v3 <= v1).get must_== false
      (v3 <= v3).get must_== true
      (v2 <= v1).get must_== false
      (v2 <= v3).get must_== true
      (v2 <= v2).get must_== true
    }
    "==" in {
      (v1 == v2).get must_== false
      (v1 == v3).get must_== false
      (v1 == v1).get must_== true
      (v3 == v2).get must_== false
      (v3 == v1).get must_== false
      (v3 == v3).get must_== true
      (v2 == v1).get must_== false
      (v2 == v3).get must_== false
      (v2 == v2).get must_== true
    }
  }
}