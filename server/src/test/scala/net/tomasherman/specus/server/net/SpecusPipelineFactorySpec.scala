package net.tomasherman.specus.server.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import org.specs2.matcher.ThrownExpectations
import org.jboss.netty.channel.ChannelHandler

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

class SPFEnv(val channelHandler:ChannelHandler,val channelEncoder:ChannelHandler,val channelDecoder:ChannelHandler)

trait SpecusPipelineFactoryScope extends Scope with Mockito with ThrownExpectations {
  val handler = mock[SpecusHandler]
  val encoder = mock[SpecusEncoder]
  val decoder = mock[SpecusDecoder]

  val env = new SPFEnv(handler,encoder,decoder)
  val factory = new SpecusPipelineFactory(env)
}

class SpecusPipelineFactorySpec extends Specification {
  "SpecusPipelineFactory" should {
    "DI correctly" in new SpecusPipelineFactoryScope {
      success //this test is here just to make sure that DI will work even when a subtype of a class is actually wanted ...
              //for example SpecusPipelineFactory wants ChannelHandlers but gets Specus{Handler,Encoder,Decoder}
    }
  }
}