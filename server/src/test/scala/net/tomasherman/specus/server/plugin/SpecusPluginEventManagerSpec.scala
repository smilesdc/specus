package net.tomasherman.specus.server.plugin

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import net.tomasherman.specus.server.api.plugin.PluginEvent
import akka.actor.ActorRef
import collection.immutable.Map

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

class e1 extends PluginEvent
class e2 extends PluginEvent
class e3 extends PluginEvent
class TestableSPEM extends SpecusPluginEventManager {
  def getMapping = mapping
}
trait SPEMScope extends Scope with Mockito{
  val mgr = new TestableSPEM

  val p1 = mock[ActorRef]
  val p2 = mock[ActorRef]

  val e1 = classOf[e1]
  val e2 = classOf[e2]
  val e3 = classOf[e3]
  
  val dep1 = List[Class[_<:PluginEvent]](e1,e2)
  val dep2 = List[Class[_<:PluginEvent]](e3,e2)

  val exp1 = List(p1)
  val exp2 = List(p2)
  val exp12 = List(p1,p2)

}


class SpecusPluginEventManagerSpec extends Specification {

    "SpecusPluginEventManager" should {
      "register processors properly" in new SPEMScope {
        mgr.registerEventProcessor(p1,dep1)
        mgr.getMapping must_== Map((e1,exp1),(e2,exp1))
        mgr.registerEventProcessor(p2,dep2)
        mgr.getMapping must_== Map((e1,exp1),(e2,exp12),(e3,exp2))
      }
      "remove processors properly" in new SPEMScope {
        mgr.getMapping.+=((e1,exp1),(e2,exp12),(e3,exp2))
        mgr.removeEventProcessor(p1)
        mgr.getMapping must_== Map((e1,List()),(e2,exp2),(e3,exp2))
        mgr.removeEventProcessor(p2)
        mgr.getMapping must_== Map((e1,List()),(e2,List()),(e3,List() ))
      }
    }

}