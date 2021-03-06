package net.tomasherman.specus.server.net

import org.jboss.netty.channel._

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

/** ChannelPipelineFactory that returns injected Encoder,Decoder and Handler. */
class SpecusPipelineFactory(env: ({
  val channelEncoder: ChannelHandler
  val channelDecoder: ChannelHandler
  val channelHandler: ChannelHandler
})) extends ChannelPipelineFactory{
  def getPipeline = new StaticChannelPipeline(env.channelEncoder,env.channelDecoder,env.channelHandler)
}