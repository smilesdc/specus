package net.tomasherman.specus.server.plugin

import org.specs2.mutable.Specification
import java.io.File
import java.net.{URI, URL}
import net.tomasherman.specus.server.plugin.PluginDefinitionLoading.{parsePluginDefinition}
import net.tomasherman.specus.server.api.plugin.{PluginDefinition, PluginDefinitionFileNotFound, PluginDefinitionParsingFailed}

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

class PluginDefinitionLoadingSpec extends Specification{
  implicit def urlToUri(url:URL):URI = url.toURI

  val testValidDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/valid/"))
  val testInvalidDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/invalid/"))
  val testNonexistingDir= new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins/nonexisting/"))

  "PluginDefinitionLoading" should {
    "parsePluginDefinition" in {
      parsePluginDefinition(testValidDir) must_== new PluginDefinition("test_name","1.3.3.7","test_author","plugin.PluginDefinitionLoading.plugins.valid.DummyPlugin")
    }
    "parseInvalidDefinition" in {
      parsePluginDefinition(testInvalidDir) must throwAn[PluginDefinitionParsingFailed]
    }
    "parseNonexistingDefinition" in {
      parsePluginDefinition(testNonexistingDir) must throwAn[PluginDefinitionFileNotFound]
    }
  }
}