package net.tomasherman.specus.server.net

import org.jboss.netty.buffer.ChannelBuffer
import net.tomasherman.specus.server.api.net._
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

/** Implementation of ProtocolDecoder using CodecRepository and Codecs. It
  * expects to be mixed in with a class into which was injected a CodecRepository */
trait CodecBasedProtocolDecoder extends ProtocolDecoder {
  val env: {val codecRepository: CodecRepository}

  def decode(buffer: ChannelBuffer) = {
    val packetId = buffer.readByte()
    env.codecRepository.lookupCodec(packetId) match {
      case Some(x) => x.decode(buffer)
      case None => throw new BufferDecoderNotFoundException(packetId)
    }
  }
}