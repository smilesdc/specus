package net.tomasherman.specus.server.plugin.definitions

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.plugin.definitions.PluginVersion

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
class MajorMinorBuildPluginVersionSpec extends Specification with Mockito with ThrownExpectations{
  type mmb = MajorMinorBuildPluginVersion
  "MajorMinorBuildPluginVersion" should {
    "canCompare properly" in {
      val v1 = new mmb(1,3,3)
      val v2 = mock[PluginVersion]
      val v3 = new mmb(1,3,3)

      v1.canCompare(v2) must_== false
      v1.canCompare(v3) must_== true
      v3.canCompare(v1) must_== true
      v2.canCompare(v1) must_== false
      v2.canCompare(v3) must_== false
    }
    "compare properly" in {
      val v1 = new mmb(1,1,1)
      val v2 = new mmb(1,1,2)
      val v3 = new mmb(1,2,1)
      val v4 = new mmb(1,1)
      val v5 = new mmb(1,2)
      val v6 = new mmb(2,1)
      val v7 = new mmb(1)
      val v8 = new mmb(2)

      v1.compare(v2) must_== Some(-1)
      v1.compare(v1) must_== Some(0)
      v1.compare(v3) must_== Some(-1)
      v1.compare(v4) must_== Some(0)
      v1.compare(v5) must_== Some(-1)
      v1.compare(v6) must_== Some(-1)
      v1.compare(v7) must_== Some(0)
      v1.compare(v8) must_== Some(-1)

      v6.compare(v8) must_== Some(0)
      v6.compare(v7) must_== Some(1)
      v6.compare(v5) must_== Some(1)
      v6.compare(v6) must_== Some(0)

      v3.compare(v6) must_== Some(-1)
      v3.compare(v5) must_== Some(0)
    }
  }

}