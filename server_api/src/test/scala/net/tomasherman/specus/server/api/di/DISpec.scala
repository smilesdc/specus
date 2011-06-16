package net.tomasherman.specus.server.api.di

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import net.tomasherman.specus.server.api.net.CodecRepository

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

class DependentClass(env:{val codecRepository:CodecRepository}){
  def codecRepository = env.codecRepository
}
class TestConfigRepository extends ConfigRepositroyTrait

class DISpec extends Specification with Mockito{
  val m = mock[Config]
  val cm = mock[CodecRepository]
  val MOCK_MSG = "this is a mock"
  cm.toString returns MOCK_MSG
  m.codecRepository returns cm

  "DependencyInjection" should {
    "(mokcing test)" in {
      m.codecRepository.toString must_== MOCK_MSG
    }

    "exception when config not set" in {
      val conf = new TestConfigRepository
      conf() must throwA[NoDependencyConfigurationSupplied]
    }

    "structural type injecting" in {
      val conf = new TestConfigRepository

      conf.config = m
      val dc = new DependentClass(conf())
      dc.codecRepository.toString must_== MOCK_MSG
    }

    "exception when config assigned twice" in {
      val conf = new TestConfigRepository
      conf.config = m
      (conf.config = m) must throwA [ConfigAlreadyLockedDown]
    }



  }
}