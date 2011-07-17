package net.tomasherman.specus.server.plugin

import java.io.File
import net.tomasherman.specus.server.plugin.PluginDefinitionLoading._
import net.tomasherman.specus.server.api.logging.Logging
import net.tomasherman.specus.server.api.config.Configuration
import net.tomasherman.specus.server.api.plugin._
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
class SimplePluginManager(val env: {val config: Configuration}) extends PluginManager with Logging{
  private var plugins: List[Plugin] = List[Plugin]()

  /** Loads all valid plugins from given directory.
    * @param dir Directory in which the plugins are looked up.
    * @returns List of loaded plugins.
    */
  def bootupPlugins(dir: File): List[Plugin] = {
    val dirs = dir.listFiles().filter(_.isDirectory).toList
    plugins = dirs.flatMap(handleExceptions(_, loadPlugin)) // loadPlugin is partially applied!
    getPlugins
  }

  def getPlugins = plugins

  /** Helper plugin-loading function to make bootupPlugins a little more readable.
    * Handles PluginDefinitions loading as well as instantiating of new Plugin class
    * @param f Directory in which the Plugin definitions file is expected to be.
    * @return Plugin instance.
    */
  private val loadPlugin = {f: File =>
    val pd = loadPluginFromDir(f,env.config.plugin.pluginDefinitionFileName)
    instantiatePlugin(pd.pluginClass)
  }

  /** Creates new instance of class.
    * @param String representation of the class to be created.
    * @returns New instance of the desired class.
    */
  private def instantiatePlugin(c: String): Plugin = {
    Class.forName(c).newInstance().asInstanceOf[Plugin]
  }

  /** Helper function for nicer exception handling.
    * @param file File that is being passed to the *in* function.
    * @param in Function that tries to
    * @return  
    */
  private def handleExceptions(file: File,in: File => Plugin) = {
    try {
      Some(in(file))
    } catch {
      case e:PluginDefinitionFileNotFound => error("Error during plugin-loading - Plugin definitions not found",e); None
      case e:PluginDefinitionParsingFailed => error("Error during plugin-loading - Plugin definitions parsing failed",e); None
      case e:ClassNotFoundException => error("Error during plugin-loading - Plugin class not found",e); None
    }
  }
}