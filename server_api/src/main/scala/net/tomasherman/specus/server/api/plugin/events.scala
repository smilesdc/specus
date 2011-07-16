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

/** Abstraction of all the messages passed to PluginEventProcessors */
abstract class PluginEvent

/** Announces that a command registered by plugin was invoked.
  * @param prefix Prefix of the command, e.g. 'ls'
  * @param arg Arguments with which the command has been invoked. e.g. '-l' */
class CommandInvocation(prefix: String, arg: String) extends PluginEvent