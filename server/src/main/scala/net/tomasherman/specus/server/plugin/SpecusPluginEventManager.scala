package net.tomasherman.specus.server.plugin

import collection.mutable.Map
import akka.actor.ActorRef
import net.tomasherman.specus.server.api.plugin.{PluginEventProcessorId, PluginEvent, PluginEventManager}

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
class SpecusPluginEventManager extends PluginEventManager{

  protected val mapping = Map[Class[_],List[PluginEventProcessorId]]()
  protected val idToProcessor = Map[PluginEventProcessorId,ActorRef]()
  protected var lastId = 0

  def registerEventProcessor(processor:ActorRef, events:List[Class[_<:PluginEvent]]) = {
    lastId = lastId+1
    val newId = IntPluginEventProcessorId(lastId)
    events.foreach({ e =>
      val newList = mapping.getOrElse(e,List[PluginEventProcessorId]()) ::: List(newId)
      idToProcessor(newId) = processor
      mapping(e) = newList
    })
    newId
  }

  def removeEventProcessor(processor:PluginEventProcessorId) {
    mapping.filter({ p => p._2.contains(processor)}).foreach( { q => mapping(q._1) = q._2.filterNot({ x => x == processor })})
  }

  def sendEvent(event:PluginEvent) {
    mapping(event.getClass).foreach({ p => sendToProcessor(event,p)})
  }

  def sendToProcessor(event: PluginEvent, processor: PluginEventProcessorId) {
    idToProcessor(processor) ! event
  }
}