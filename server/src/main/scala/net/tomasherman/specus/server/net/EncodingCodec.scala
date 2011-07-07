package net.tomasherman.specus.server.net

import net.tomasherman.specus.server.api.net.Codec
import net.tomasherman.specus.common.api.net.Packet
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
abstract class EncodingCodec[T<:Packet](packetId:Byte,packetClass:Class[T]) extends Codec(packetId,packetClass){

  def encode(packet:T) = {
    val buffer = createChannelBuffer
    buffer.writeByte(packetId)
    encodeDataToBuffer(packet,buffer)
    buffer
  }

  protected def createChannelBuffer:ChannelBuffer
  protected def encodeDataToBuffer(packet:T,buffer:ChannelBuffer):Unit
}