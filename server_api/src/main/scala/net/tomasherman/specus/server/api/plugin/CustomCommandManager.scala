package net.tomasherman.specus.server.api.plugin

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

/** CustomCommand container that keeps track of what PluginEventProcessor
  * should be called when the command is invoked. */
trait CustomCommandManager {
  //TODO add lookup both  and options
  /** Adds command to the container.
    * @param cmd CustomCommand to be added.
    * @param processor PluginEventProcessorId id of processor to be notified when
    * the command is invoked */
  def registerCommand(cmd: CustomCommand, processor: PluginEventProcessorId)

  /** Removes command from the container.
    * @param prefix Prefix of command to be removed */
  def removeCommand(prefix: String)

  /** Looks up command.
    * @param prefix Prefix of a command to be found.
    * @return Command */
  def lookupCommand(prefix: String): CustomCommand

  /** Looks up processor for prefix.
    * @param prefix Prefix of command whose processor will be looked up.
    */
  def lookupProcessor(prefix: String): PluginEventProcessorId
}