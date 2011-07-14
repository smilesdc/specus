package net.tomasherman.specus.server.plugin

import collection.mutable.Map
import net.tomasherman.specus.server.api.plugin.{PluginEvent, PluginEventManager}
import akka.actor.{ActorRef}

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

  private val mapping = Map[Class[_],List[ActorRef]]()

  def registerEventProcessor(processor:ActorRef, events:List[Class[_<:PluginEvent]]) {
    events.foreach({ e =>
      val newList = mapping.getOrElse(e,List[ActorRef]()) ::: List(processor)
      mapping(e) = newList
    })
  }

  def removeEventProcessor(processor:ActorRef) {
    mapping.filter({ p => p._2.contains(processor)}).foreach( { q => mapping(q._1) = q._2.filterNot({ x => x == processor })})
  }

  def sendEvent(event:PluginEvent) {
    mapping(event.getClass).foreach({ p => p ! event})
  }

}