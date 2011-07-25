package net.tomasherman.specus.server.plugin

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import akka.actor.ActorRef
import collection.immutable.Map
import net.tomasherman.specus.server.api.plugin.{PluginEventProcessorId, PluginEvent}

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
  def getIdmapping = idToProcessor
  def setMapping(map:Map[Class[_],List[PluginEventProcessorId]]) {this.mapping = map}
  def setIDmapping(map:Map[PluginEventProcessorId,ActorRef]) {this.idToProcessor = map}
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

  val id1 =  new IntPluginEventProcessorID(1)
  val id2 =  new IntPluginEventProcessorID(2)
  val exp1 = List(id1)
  val exp2 = List(id2)
  val exp12 = List(id1,id2)

}


class SpecusPluginEventManagerSpec extends Specification {

    "SpecusPluginEventManager" should {
      "register processors properly" in new SPEMScope {
        mgr.registerEventProcessor(p1,dep1)
        mgr.getMapping must_== Map((e1,exp1),(e2,exp1))
        mgr.getIdmapping must_== Map((id1,p1))

        mgr.registerEventProcessor(p2,dep2)
        mgr.getMapping must_== Map((e1,exp1),(e2,exp12),(e3,exp2))
        mgr.getIdmapping must_== Map((id1,p1),(id2,p2))
      }

      "remove processors properly" in new SPEMScope {
        mgr.setMapping(Map((e1,exp1),(e2,exp12),(e3,exp2)))
        mgr.setIDmapping(Map((id1,p1),(id2,p2)))
        mgr.removeEventProcessor(id1)
        mgr.getIdmapping must_== Map((id2,p2))
        mgr.getMapping must_== Map((e1,List()),(e2,exp2),(e3,exp2))

        mgr.removeEventProcessor(id2)
        mgr.getIdmapping must_== Map()
        mgr.getMapping must_== Map((e1,List()),(e2,List()),(e3,List() ))
      }
    }

}