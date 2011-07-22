package net.tomasherman.specus.server.plugin.definitions

import java.io.File
import net.tomasherman.specus.server.api.config.Configuration
import io.Source
import net.liftweb.json._
import net.tomasherman.specus.server.api.plugin.definitions._
import net.tomasherman.specus.server.plugin.definitions.ParserCombinatorsVersionConstraintParser.OK

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

class JsonPluginDefinitionLoader(val env: {val config:Configuration; val pluginVersionParser: PluginVersionConstraintParser}) extends PluginDefinitionLoader{
  def parsePluginDefinition(pdFile: File) = {
    val data = Source.fromFile(pdFile).getLines().mkString
    buildDefinitions(parse(data))
  }
  private def buildDefinitions(data:JValue) = {
    val JString(author) = data \ env.config.plugin.definitions.authorKey
    val JString(name) = data \ env.config.plugin.definitions.nameKey
    val JString(identifier) = data \ env.config.plugin.definitions.identifierKey
    val JString(version) = data \ env.config.plugin.definitions.versionKey
    val JString(pluginClass) = data \ env.config.plugin.definitions.pluginClassKey
    val JArray(dependencies) = data \ env.config.plugin.definitions.dependenciesKey

    new PluginDefinition(
    name,new StringPluginIdentifier(identifier),MajorMinorBuildPluginVersion(version).get,author,pluginClass,new PluginDependencies(dependencies.map(buildDependency(_)))
    )
  }

  private def buildDependency(data: JValue) = {
    val JString(identifier) = data \ env.config.plugin.definitions.dependency_identifier
    val JString(version) = data \ env.config.plugin.definitions.dependency_version

    val id = new StringPluginIdentifier(identifier)
    val dep = ParserCombinatorsVersionConstraintParser.parse(version) match {
      case OK(x) => x
      case _ => null
    }
    new PluginDependency(id,dep)
  }
}