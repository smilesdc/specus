package net.tomasherman.specus.server.api.net

import packet.Packet
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


/**
 * Abstract trait codec needs to implement. There is one codec per packet type. *Codec must be stateless*!
 * @param packetId Id of the packet. First byte in every packet.
 * @param packetClass Class of the packet returned by the codec.
 */
abstract class Codec[T<:Packet](val packetId:Byte,val packetClass:Class[T]){
  /**
   * Encodes particular packet into ChannelBuffer ready to be send through the network.
   * @param packet Packet to be encoded
   */
  def encode(packet:T):ChannelBuffer

  /**
   * Attempts to decode packet class from the ChannelBuffer.
   * @param buffer ChannelBuffer, from which codec is meant to be decoding the class.
   */
  def decode(buffer:ChannelBuffer):T
}

/**
 * Trait encapsulating all methods required for looking up Codecs by the packet id or packet class.
 */
