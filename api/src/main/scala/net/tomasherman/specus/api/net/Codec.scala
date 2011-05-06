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
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 */



abstract class Codec[T<:Packet](val packetId:Byte,val packetClass:Class[T]){

  def encode(packet:T)
  def decode(buffer:ChannelBuffer):T
}

trait CodecRepository{
  import scala.collection.mutable.Map
  val codecByIDMap = Map[Byte,Codec[_]]()
  val codecByPacketMap = Map[Class[_],Codec[_]]()

  def lookupCodec(packetId:Byte) = codecByIDMap.get(packetId)
  def lookupCodec(p:Packet) = codecByPacketMap.get(p.getClass)

  def registerCodec[C<:Codec[_<:Packet]](codecClass:Class[C]) {
    val instance = codecClass.newInstance
    codecByPacketMap(instance.packetClass) = instance
    codecByIDMap(instance.packetId) = instance
  }
}