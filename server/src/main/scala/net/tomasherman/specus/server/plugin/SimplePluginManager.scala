package net.tomasherman.specus.server.plugin

import java.io.File
import net.tomasherman.specus.server.api.logging.Logging
import net.tomasherman.specus.server.api.config.Configuration
import net.tomasherman.specus.server.api.plugin._
import net.tomasherman.specus.server.api.plugin.definitions.{PluginDefinition, PluginVersionMatchingException, PluginIdentifier, PluginDefinitionLoader}

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


/** Implementation of PluginManager. Expects to be injected with Configuration */
class SimplePluginManager(val env: {
  val config: Configuration
  val pluginDefinitionLoader: PluginDefinitionLoader
}) extends PluginManager with Logging{

  private var plugins = Map[PluginIdentifier,(PluginDefinition,Plugin)]()

  /** Loads all valid plugins from given directory.
    * @param dir Directory in which the plugins are looked up.
    * @returns List of loaded plugins. */
  def bootupPlugins(dir: File) {
    val dirs = dir.listFiles().filter(_.isDirectory).toList
    plugins = dirs.map(loadPlugin(_)).toMap

  }

  def checkPluginDependencies = {
    plugins.keys.foldLeft(true)({ (_,p) => //check if all the plugin dependencies are satisfied
      plugins(p)._1.dependencies.dep.foldLeft(true)({ (_,d) =>  //check each the dependency is satisfied
        d.version.matches(plugins(d.identifier)._1.version) match {
          case Some(x) => x
          case None => throw new PluginVersionMatchingException(d.version,plugins(d.identifier)._1.version)
        }
      })
    })
  }

  def getPluginIdentifiers = plugins.keySet

  /** Helper plugin-loading function to make bootupPlugins a little more readable.
    * Handles PluginDefinitions loading as well as instantiating of new Plugin class
    * @param f Directory in which the Plugin definitions file is expected to be.
    * @return Plugin instance. */
  private val loadPlugin = {f: File =>
    val pd = env.pluginDefinitionLoader.loadPluginFromDir(f,env.config.plugin.pluginDefinitionFileName)
    (pd.identifier,(pd,instantiatePlugin(pd.pluginClass)))
  }

  /** Creates new instance of class.
    * @param String representation of the class to be created.
    * @returns New instance of the desired class. */
  private def instantiatePlugin(c: String) = {
    Class.forName(c).newInstance().asInstanceOf[Plugin]
  }
}