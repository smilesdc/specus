package net.tomasherman.specus.server.plugin

import java.io.File
import io.Source
import net.liftweb.json._
import net.tomasherman.specus.server.api.plugin.{PluginDefinition, PluginDefinitionParsingFailed}

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

/** Fucntions for LoadingPlugins */
object PluginDefinitionLoading{
  implicit val formats = DefaultFormats

  /** Attempts to lookup and parse plugin definitions file.
    * @param dir Directory in which the file is being looked up.
    * @param pdfName File name of plugin definitions,
    * @throws MappingException Thrown when parsing fails.
    * @returns Parsed PluginDefinition instance
    */
  def lookupFile(dir:File,pdfName:String) = {
    dir.listFiles() find(_.getName == pdfName)
  }

  def parsePluginDefinition(pdfFile:File):PluginDefinition = {
    try{
      parse(Source.fromFile(pdfFile).getLines().mkString).extract[PluginDefinition]
    } catch {
      case e: MappingException => throw new PluginDefinitionParsingFailed(pdfFile,e)
    }
  }
}