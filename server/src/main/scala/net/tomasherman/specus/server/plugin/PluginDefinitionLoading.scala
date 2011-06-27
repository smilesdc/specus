package net.tomasherman.specus.server.plugin

import java.io.{File}
import io.Source
import net.liftweb.json._
import net.tomasherman.specus.server.api.Constants
import net.tomasherman.specus.server.api.plugin.{PluginDefinition, PluginDefinitionParsingFailed, PluginDefinitionFileNotFound}

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

object PluginDefinitionLoading{
  implicit val formats = DefaultFormats

  def parsePluginDefinition(dir:File):PluginDefinition = {
    dir.list.find( p => p == Constants.Plugin.pluginDefinitionsFileName ) match {
      case None => throw new PluginDefinitionFileNotFound(dir)
      case Some(path) => {
        try{
          parse(Source.fromFile(new File(dir,path)).getLines().mkString).extract[PluginDefinition]
        } catch {
          case e:MappingException => throw new PluginDefinitionParsingFailed(dir,e)
        }
      }
    }
  }
}