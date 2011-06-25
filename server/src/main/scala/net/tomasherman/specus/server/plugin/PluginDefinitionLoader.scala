package net.tomasherman.specus.server.plugin

import java.io.{File}
import io.Source
import net.liftweb.json._
import com.weiglewilczek.slf4s.Logging


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

class PluginDefinitionLoader extends Logging{
  implicit val formats = DefaultFormats

  def loadAllPlugins(pluginDirectory:File):List[PluginDefinition] = {
    pluginDirectory.listFiles.filter( p => p.isDirectory ).map( d => parsePluginDefinition(d) ).flatMap(
      p => p match {
        case None => Nil
        case Some(x) => List(x)
      }).toList
  }
  def parsePluginDefinition(dir:File):Option[PluginDefinition] = {
    dir.list().find( p => p == "plugin.json" ) match {
      case None => {logger.error("plugin.json not found");None}
      case Some(path) => {
        Some(parse(Source.fromFile(new File(dir,path)).getLines().mkString).extract[PluginDefinition])
      }
    }
  }
}