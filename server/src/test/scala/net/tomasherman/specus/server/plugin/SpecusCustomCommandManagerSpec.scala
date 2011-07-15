package net.tomasherman.specus.server.plugin

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import net.tomasherman.specus.server.api.plugin.{PluginEventProcessorId, CustomCommand}
import org.specs2.matcher.ThrownExpectations

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

class ShamelessSCCMScope extends SpecusCustomCommandManager{
  def getMap = commands
}

trait SCCMScope extends Scope with Mockito with ThrownExpectations{
  val mgr = new ShamelessSCCMScope
  val s1 = "s1"
  val d1 = "d1"

  val s2 = "s2"
  val d2 = "d2"

  val c1 = new CustomCommand(s1,d1)
  val c2 = new CustomCommand(s2,d2)

  val id1 = mock[PluginEventProcessorId]
  val id2 = mock[PluginEventProcessorId]
}

class SpecusCustomCommandManagerSpec extends Specification{
  "SpecusCustomCommandManager" should {
    "register command properly" in new SCCMScope {
      mgr.registerCommand(c1,id1)
      mgr.getMap must_== Map((s1,(c1,id1)))
      mgr.registerCommand(c2,id2)
      mgr.getMap must_== Map((s1,(c1,id1)),(s2,(c2,id2)))
    }
    "remove command properly" in new SCCMScope {
      mgr.getMap.+=((s1,(c1,id1)),(s2,(c2,id2)))
      mgr.removeCommand(s1)
      mgr.getMap must_== Map((s2,(c2,id2)))
      mgr.removeCommand(s2)
      mgr.getMap must_== Map()
    }
    "lookup stuff prorperly" in new SCCMScope {
      mgr.getMap.+=((s1,(c1,id1)),(s2,(c2,id2)))
      mgr.lookupCommand(s1) must_== c1
      mgr.lookupCommand(s2) must_== c2
      mgr.lookupProcessor(s1) must_== id1
      mgr.lookupProcessor(s2) must_== id2
    }
  }
}