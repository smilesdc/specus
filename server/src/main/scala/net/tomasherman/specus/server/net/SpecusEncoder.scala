package net.tomasherman.specus.server.net

import org.jboss.netty.handler.codec.oneone.OneToOneEncoder
import org.jboss.netty.channel.{Channel, ChannelHandlerContext}
import net.tomasherman.specus.common.api.net.Packet
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
class SpecusEncoder(val env: {val codecRepository: CodecRepository})
  extends OneToOneEncoder with CodecBasedProtocolEncoder{

  def encode(ctx: ChannelHandlerContext, channel: Channel, msg: AnyRef) = {
    encode(msg.asInstanceOf[Packet])
  }
}