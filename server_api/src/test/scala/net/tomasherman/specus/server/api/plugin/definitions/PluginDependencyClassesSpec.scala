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


class PluginDependencyClassesSpec extends Specification with Mockito{

  "Interval" should {
    "equal properly" in {
      val v1 = mock[PluginVersion]
      val v2 = mock[PluginVersion]
      val i1 = new Interval(v1,v2)
      i1 must_== i1
    }
  }
}