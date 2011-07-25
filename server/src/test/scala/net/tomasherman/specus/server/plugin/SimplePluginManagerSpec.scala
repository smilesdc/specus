package net.tomasherman.specus.server.plugin

import definitions.MajorMinorBuildPluginVersion
import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import net.tomasherman.specus.server.api.di.DependencyConfig
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.Scope
import net.tomasherman.specus.server.api.plugin.definitions._
import net.tomasherman.specus.server.api.plugin.Plugin

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

trait SPMScope extends Scope with Mockito with ThrownExpectations{
  type mmb = MajorMinorBuildPluginVersion
  val di = mock[DependencyConfig]
  val pm = new SimplePluginManager(di)

  val i1 = mock[PluginIdentifier]
  val v1 = new mmb(1)
  val i2 = mock[PluginIdentifier]
  val v2 = new mmb(2)
  val i3 = mock[PluginIdentifier]
  val v3 = new mmb(1,1,5)

  val defin1 = mock[PluginDefinition]
  val dep1 = new PluginDependency(i2,new EqGt(new mmb(1)))
  val deps1 = new PluginDependencies(List(dep1))
  defin1.dependencies returns deps1
  defin1.version returns v1

  val defin2 = mock[PluginDefinition]
  val dep2 = new PluginDependency(i3,new Interval(new mmb(1),new mmb(2)))
  val deps2 = new PluginDependencies(List(dep2))
  defin2.dependencies returns deps2
  defin2.version returns v2

  val defin3 = mock[PluginDefinition]
  val dep3 = new PluginDependency(i1,new EqGt(new mmb(1)))
  val deps3 = new PluginDependencies(List(dep3))
  defin3.dependencies returns deps3
  defin3.version returns v3

  val p = mock[Plugin]
}

class SimplePluginManagerSpec extends Specification{
  "Simple plugin manager" should {
    "check plugin dependencies" in new SPMScope {
      pm.checkPluginDependencies(Map((i1,(defin1,p)),(i2,(defin2,p)),(i3,(defin3,p)))) //shouldn't throw any exceptions
    }
    "invalid dependency" in new SPMScope {
        val i4 = mock[PluginIdentifier]
        val v4 = new mmb(1,1,5)
        val defin4 = mock[PluginDefinition]
        val dep4 = new PluginDependency(i1,new EqGt(new mmb(1000))) //this should fail
        val deps4 = new PluginDependencies(List(dep4))
        defin4.dependencies returns deps4
        defin4.version returns v4
      pm.checkPluginDependencies(
        Map((i1,(defin1,p)),(i2,(defin2,p)),(i3,(defin3,p)),(i4,(defin4,p)))
      ) must throwA[PluginVersionMatchingException]
    }
  }
}