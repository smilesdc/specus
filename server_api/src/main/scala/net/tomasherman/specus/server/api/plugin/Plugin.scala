package net.tomasherman.specus.server.api.plugin

import net.tomasherman.specus.server.api.net.Codec
import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.server.api.di.DependencyConfig
import akka.actor.Actor

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

/** Trait encapsulating data about plugins. */
trait Plugin {

  /** Chance for plugin to initialize it's stuff, start up actors etc. */
  def initialize(dependencies: DependencyConfig) {}

  /** Getter for classes of codecs to be handled by a server.
    * @return Option of List of classes to be handled.
    */
  def getCodecs: Option[List[Class[_ <: Codec[_ <: Packet]]]]

  /** Returns Class of A
    *
    */
  def eventProcessorClass: Option[Class[_<:Actor]]
  def registerForEvents: Option[List[Class[_ <: PluginEvent]]]
  def customCommands: Option[List[CustomCommand]]

  override def equals(that: Any) = {
    if (that.isInstanceOf[Object]) {
      this.getClass == that.asInstanceOf[Object].getClass
    } else {
      false
    }
  }
  override def hashCode() = {
    this.getClass.getName.hashCode() * 41
  }

}