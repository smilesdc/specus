package net.tomasherman.specus.server.net

import net.tomasherman.specus.server.api.logging.Logging
import org.jboss.netty.channel.{Channel, ChannelHandlerContext}
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.handler.codec.replay.{VoidEnum, ReplayingDecoder}
import net.tomasherman.specus.server.api.net.{CodecRepository}

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


class SpecusDecoder(val env:{val codecRepository:CodecRepository}) extends ReplayingDecoder[VoidEnum] with CodecBasedProtocolDecoder with Logging{
  def decode(ctx: ChannelHandlerContext, channel: Channel, buffer: ChannelBuffer, state: VoidEnum) = {
    try{
      decode(buffer)
    }
  }
}