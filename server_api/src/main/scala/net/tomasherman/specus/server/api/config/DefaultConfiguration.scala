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

/** Default configuration to be extended by user configurations. */
class DefaultConfiguration extends Configuration {
  val plugin = new PluginConfig {
    val pluginDefinitionFileName = "plugin.json"
    val directory = "plugins"
    val definitions = new PluginDefinitionsConfig {
      val identifierKey = "identifier"
      val versionKey = "version"
      val nameKey = "name"
      val pluginClassKey = "pluginClass"
      val authorKey = "author"
      val dependenciesKey = "dependencies"
      val dependency_version = "version"
      val dependency_identifier = "identifier"
    }
  }
}