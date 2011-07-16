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

/** Container of Plugin definition data.
  * @param name Name of the plugin
  * @param version Version of the plugin
  * @param author Author of the plugin
  * @param pluginClass String representation of a plugin entry point
  */
case class PluginDefinition(
  name: String,
  version: String,
  author: String,
  pluginClass: String)