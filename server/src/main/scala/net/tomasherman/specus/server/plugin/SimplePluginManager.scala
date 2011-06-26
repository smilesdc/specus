package net.tomasherman.specus.server.plugin

import java.io.File
import net.tomasherman.specus.server.plugin.PluginDefinitionLoading.parsePluginDefinition
import collection.mutable.ListBuffer
import com.weiglewilczek.slf4s.Logging
import net.tomasherman.specus.server.api.plugin.{PluginDefinitionParsingFailed, PluginDefinitionFileNotFound, Plugin, PluginManager}

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

class SimplePluginManager extends PluginManager with Logging{
  private var plugins:List[Plugin] = List[Plugin]()

  def bootupPlugins(dir: File):List[Plugin] = {
    val pbuffer = ListBuffer[Plugin]()
    for( x <- dir.listFiles.filter( p => p.isDirectory )){
      try{
        val pdef = parsePluginDefinition(x)
        val pluginClass = Class.forName(pdef.pluginClass).newInstance().asInstanceOf[Plugin]
        pbuffer.append(pluginClass)
      } catch {
        case e:PluginDefinitionFileNotFound => logger.error("Error during plugin-loading - Plugin definitions not found",e)
        case e:PluginDefinitionParsingFailed => logger.error("Error during plugin-loading - Plugin definitions parsing failed",e)
        case e:ClassNotFoundException => logger.error("Error during plugin-loading - Plugin class not found",e)
      }
    }
    plugins = pbuffer.toList
    getPlugins
  }
  def getPlugins = plugins

}