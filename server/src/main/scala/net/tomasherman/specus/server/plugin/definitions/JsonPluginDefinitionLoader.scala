package net.tomasherman.specus.server.plugin.definitions

import java.io.File
import net.tomasherman.specus.server.api.config.Configuration
import io.Source
import net.liftweb.json._
import net.tomasherman.specus.server.api.plugin.definitions._
import net.tomasherman.specus.server.plugin.definitions.ParserCombinatorsVersionConstraintParser.{Fail, IntervalParsed, EqGtParsed}

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

class JsonPluginDefinitionLoader(val env: {val config:Configuration}) extends PluginDefinitionLoader{
  def parsePluginDefinition(pdFile: File) = {
    val data = Source.fromFile(pdFile).getLines().mkString
    buildDefinitions(parse(data))
  }
  private def buildDefinitions(data:JValue) = {
    val author = getStringWithEx(data,env.config.plugin.definitions.authorKey)
    val name = getStringWithEx(data,env.config.plugin.definitions.nameKey)
    val identifier = getStringWithEx(data,env.config.plugin.definitions.identifierKey)
    val version = getStringWithEx(data,env.config.plugin.definitions.versionKey)
    val pluginClass = getStringWithEx(data,env.config.plugin.definitions.pluginClassKey)
    val dependencies = getListWithEx(data,env.config.plugin.definitions.dependenciesKey)

    val dep = dependencies.map(buildDependency(_))

    new PluginDefinition(
      name,
      new StringPluginIdentifier(identifier),
      MajorMinorBuildPluginVersion(version).get,
      author,
      pluginClass,
      new PluginDependencies(dep)
    )
  }

  private def getJsonFieldWithEx(data: JValue, key: String) = {
    data \ key match {
      case JNothing => throw new PluginDefinitionParsingFailed("Field " + key + "wasn't found.")
      case x => x
    }
  }

  private def getStringWithEx(data: JValue,key: String) = {
    getJsonFieldWithEx(data,key) match {
      case JString(x) => x
      case _ => throw new PluginDefinitionParsingFailed("Field " + key + " is supposed to be string!")
    }
  }

  private def getListWithEx(data: JValue,key: String) = {
    getJsonFieldWithEx(data,key) match {
      case JArray(x) => x
      case _ => throw new PluginDefinitionParsingFailed("Field " + key + " is supposed to be array!")
    }
  }

  private def buildDependency(data: JValue) = {
    val JString(identifier) = data \ env.config.plugin.definitions.dependency_identifier
    val JString(version) = data \ env.config.plugin.definitions.dependency_version

    val id = new StringPluginIdentifier(identifier)
    val dep = ParserCombinatorsVersionConstraintParser.parse(version) match {
      case EqGtParsed(x) => new EqGt(x)
      case IntervalParsed(bounds) => new Interval(bounds)
      case Fail(msg) => throw new PluginDefinitionParsingFailed("Dependencies parsing failed: " + msg)
    }
    new PluginDependency(id,dep)
  }
}