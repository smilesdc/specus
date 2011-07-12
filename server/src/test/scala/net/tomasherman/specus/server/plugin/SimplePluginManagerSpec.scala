package net.tomasherman.specus.server.plugin

import org.specs2.mutable.Specification
import java.io.File
import net.tomasherman.specus.server.api.plugin.Plugin
import org.specs2.mock.Mockito
import net.tomasherman.specus.server.api.di.DependencyConfig

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

class SimplePluginManagerSpec extends Specification with Mockito{
  val testDir = new File(this.getClass.getResource("/plugin/PluginDefinitionLoading/plugins").toURI)
  val di = mock[DependencyConfig]
  di.config.plugin.pluginDefinitionFileName returns "plugin.json"
  val pm = new SimplePluginManager(di)

  "Simple plugin manager" should {
    "load plugins" in {
      pm.offTheRecords(
        {
          pm.bootupPlugins(testDir) must_== List[Plugin](new DummyPlugin)
        }
      )
    }
  }
}