package net.tomasherman.specus.server.net

import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.server.api.net._
import org.jboss.netty.buffer.ChannelBuffer

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

trait CodecBasedProtocolEncoder extends ProtocolEncoder{
  val env: {val codecRepository: CodecRepository}
  def encode(packet: Packet): ChannelBuffer = {
    env.codecRepository.lookupCodec(packet) match {
      case Some(x: Codec[_]) => x.encode(packet)
      case None => throw new PacketEncoderNotFoundException(packet)
    }
  }

}