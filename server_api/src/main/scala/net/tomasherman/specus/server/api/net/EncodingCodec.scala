package net.tomasherman.specus.server.api.net

import net.tomasherman.specus.common.api.net.Packet
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.buffer.ChannelBuffers.{dynamicBuffer,buffer}
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

/** Abstract Codec implementation providing support for easier encoding. Removes
  * the need to write packet id manually. */
abstract class EncodingCodec[T<: Packet](packetId: Byte,packetClass: Class[T],val expected:Int, val dynamic:Boolean)
  extends Codec(packetId,packetClass){

  def this(packetId: Byte,packetClass: Class[T],expected:Int) = this(packetId,packetClass,expected,false)

  def encode(packet: T) = {
    val bfr = dynamic match {
      case true => dynamicBuffer(expected+1) //1 byte for id
      case false => buffer(expected+1)
    }
    bfr.writeByte(packetId)
    encodeDataToBuffer(packet,bfr)
    bfr
  }

  /** Encodes Packet into bytes and writes them to ChannelBuffer
    * @param packet Packet to be encoded
    * @param buffer Buffer into which the bytes will be encoded*/
  protected def encodeDataToBuffer(packet: T, buffer: ChannelBuffer)
}