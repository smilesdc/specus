package net.tomasherman.specus.server.plugin

import java.io.File
import net.tomasherman.specus.server.plugin.PluginDefinitionLoading._
import collection.mutable.ListBuffer
import net.tomasherman.specus.server.api.plugin.{PluginDefinitionParsingFailed, PluginDefinitionFileNotFound, Plugin, PluginManager}
import net.tomasherman.specus.server.api.logging.Logging
import net.tomasherman.specus.server.api.config.Configuration

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
class SimplePluginManager(val env: {val config:Configuration}) extends PluginManager with Logging{
  private var plugins:List[Plugin] = List[Plugin]()
  //todo make this prettier
  def bootupPlugins(dir: File): List[Plugin] = {
    val pbuffer = ListBuffer[Plugin]()
    for( x <- dir.listFiles.filter( p => p.isDirectory )){
      try{
        lookupFile(x,env.config.plugin.pluginDefinitionFileName) match {
          case None => ()
          case Some(x) => {
            val pdef = parsePluginDefinition(x)
            pbuffer.append(Class.forName(pdef.pluginClass).newInstance().asInstanceOf[Plugin])
          }
        }
      } catch {
        case e:PluginDefinitionFileNotFound => error("Error during plugin-loading - Plugin definitions not found",e)
        case e:PluginDefinitionParsingFailed => error("Error during plugin-loading - Plugin definitions parsing failed",e)
        case e:ClassNotFoundException => error("Error during plugin-loading - Plugin class not found",e)
      }
    }
    plugins = pbuffer.toList
    getPlugins
  }
  def getPlugins = plugins

}