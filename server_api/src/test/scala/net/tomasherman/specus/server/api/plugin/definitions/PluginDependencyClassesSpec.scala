package net.tomasherman.specus.server.api.plugin.definitions

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
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
class PluginDependencyClassesSpec extends Specification with Mockito{

  "Interval" should {
    "equal properly" in {
      val v1 = mock[PluginVersion]
      val v2 = mock[PluginVersion]
      val i1 = new Interval(v1,v2)
      i1 must_== i1
    }
    "match properly" in {
      val i = new Interval(new ImplPV(1),new ImplPV(3))
      i.matches(new ImplPV(1)).get must_== true
      i.matches(new ImplPV(2)).get must_== true
      i.matches(new ImplPV(3)).get must_== false
      i.matches(new ImplPV(4)).get must_== false
    }
  }
  "EqGt" should {
    "match properly" in {
      val i = new EqGt(new ImplPV(3))
      i.matches(new ImplPV(1)).get must_== false
      i.matches(new ImplPV(2)).get must_== false
      i.matches(new ImplPV(3)).get must_== true
      i.matches(new ImplPV(4)).get must_== true
    }
  }
}