package net.tomasherman.specus.server.plugin.definitions

import org.specs2.mock.Mockito
import org.specs2.specification.Scope
import java.io.File
import net.tomasherman.specus.server.api.di.DependencyConfig
import net.tomasherman.specus.server.api.config.DefaultConfiguration
import org.specs2.mutable.Specification
import java.net.{URL, URI}
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.plugin.definitions._

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
  val config = mock[DependencyConfig]
  config.config returns new DefaultConfiguration
  val loader = new JsonPluginDefinitionLoader(config)

}


class JsonPluginDefinitionLoaderSpec extends Specification {
  "JsonPluginDefinitionLoader" should {
    "parse properly" in new JPDLScope {
      val expectedDependencies = new PluginDependencies(List(
        new PluginDependency(
          new StringPluginIdentifier("anotherid"),
          new EqGt(new MajorMinorBuildPluginVersion(1,3,3))
        ),
       new PluginDependency(
          new StringPluginIdentifier("somethingelse"),
          new Interval(
            new MajorMinorBuildPluginVersion(133,7),
            new MajorMinorBuildPluginVersion(144,7,1)
          )
        )
      ))
      val expected = new PluginDefinition(
        "test_name",
        new StringPluginIdentifier("some-identifier"),
        new MajorMinorBuildPluginVersion(1,33,7),
        "test_author",
        "net.tomasherman.specus.server.plugin.DummyPlugin",
        expectedDependencies)
      val p = loader.parsePluginDefinition(validPdf)

      p.author must_== expected.author
      p.identifier must_== expected.identifier
      p.name must_== expected.name
      p.version must_== expected.version
      p.dependencies must_== expected.dependencies
      p.pluginClass must_== expected.pluginClass

      p must_== expected
    }
  }
}
