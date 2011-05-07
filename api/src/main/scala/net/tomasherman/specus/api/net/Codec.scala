package net.tomasherman.specus.api.net

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
trait CodecRepository{
  import scala.collection.mutable.Map
  val codecByIDMap = Map[Byte,Codec[_]]()
  val codecByPacketMap = Map[Class[_],Codec[_]]()
  /**
   * Tries to find a suitable codec for packetId.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(packetId:Byte) = codecByIDMap.get(packetId)
  /**
   * Tries to find a suitable codec for packet class.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(p:Packet) = codecByPacketMap.get(p.getClass)

  /**
   * Registers codec for lookup. Note that registration fails should any other codec be registered with same packet class or packet id as registering codec.
   * @param codecClass Class of the codec to be registered. New instance is created automatically.
   * @returns True if the codec was registered correctly. False if registration failed
   */
  def registerCodec(codecClass:Class[_<:Codec[_<:Packet]]):Boolean = {
    if(codecClass == null) return false
    val instance = codecClass.newInstance
    var success = false
    if(!codecByPacketMap.contains(instance.packetClass) && !codecByIDMap.contains(instance.packetId)){
      codecByPacketMap(instance.packetClass) = instance
      codecByIDMap(instance.packetId) = instance
      success = true
    }
    success
  }
}