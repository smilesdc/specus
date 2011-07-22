package net.tomasherman.specus.server.plugin.definitions

import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import java.io.File
import net.tomasherman.specus.server.api.di.DependencyConfig
import net.tomasherman.specus.server.api.config.DefaultConfiguration
import org.specs2.mutable.Specification
import java.net.{URL, URI}
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.plugin.definitions.{PluginDefinition}

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

trait JPDLScope extends Scope with Mockito with ThrownExpectations{
  implicit def urlToUri(url:URL):URI = url.toURI

  val testValidDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/valid/"))
  val testInvalidDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/invalid/"))
  val testNonexistentDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/nonexisting/"))
  val pluginDefinitionFilename = "plugin.json"
  val validPdf = new File(testValidDir.getPath + "/" + pluginDefinitionFilename)
  val invalidPdf = new File(testInvalidDir.getPath + "/" + pluginDefinitionFilename)
  val valid = new PluginDefinition("test_name",null,null,"test_author","net.tomasherman.specus.server.plugin.DummyPlugin",null) //todo fix
  val config = mock[DependencyConfig]
  config.config returns new DefaultConfiguration
  config.pluginVersionParser returns ParserCombinatorsVersionConstraintParser
  val loader = new JsonPluginDefinitionLoader(config)

}


class JsonPluginDefinitionLoaderSpec extends Specification {
  "JsonPluginDefinitionLoader" should {
    "parse properly" in new JPDLScope {
      loader.parsePluginDefinition(validPdf) must_!= null
    }
  }
}

//  "LooklupFile" should {
//    "lookup existing file" in {
//      lookupFile(testValidDir,pluginDefinitionFilename) must_== Some(validPdf)
//    }
//    "lookup nonexisting file" in {
//      lookupFile(testNonexistentDir,pluginDefinitionFilename) must_== None
//    }
//  }
//
//  "ParsePluginDefinition" should {
//    "parse plugin definition" in {
//      parsePluginDefinition(validPdf) must_== valid
//    }
//    "parse invalid plugin definition" in {
//      parsePluginDefinition(invalidPdf) must throwA[PluginDefinitionParsingFailed]
//    }
//  }
//}