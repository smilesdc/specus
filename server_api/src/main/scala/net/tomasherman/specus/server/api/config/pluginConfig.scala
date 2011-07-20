package net.tomasherman.specus.server.api.config

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

/** Plugin related constants. */
trait PluginConfig {

  /** Folder in which can be found plugins. */
  val folder: String
  /** Name of the file containing plugin definitions.*/
  val pluginDefinitionFileName: String

  val definitions: PluginDefinitionsConfig
}

trait PluginDefinitionsConfig {
  val authorKey:String
  val versionKey:String
  val identifierKey:String
  val pluginClassKey:String
  val nameKey:String
  val dependenciesKey:String
}