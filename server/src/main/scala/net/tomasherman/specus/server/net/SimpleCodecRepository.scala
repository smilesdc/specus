package net.tomasherman.specus.server.net

import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.server.api.net.{CodecRepository, Codec}

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
 * Class encapsulating all methods required for looking up Codecs by the packet id or packet class.
 */
class SimpleCodecRepository extends CodecRepository{
  import scala.collection.mutable.Map
  val codecByIDMap = Map[Byte,Codec[_<: Packet]]()
  val codecByPacketMap = Map[Class[_],Codec[_<: Packet]]()
  /**
   * Tries to find a suitable codec for packetId.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(packetId: Byte) = codecByIDMap.get(packetId)
  /**
   * Tries to find a suitable codec for packet class.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(p: Packet) = codecByPacketMap.get(p.getClass)

  /**
   * Registers codec for lookup. Note that registration fails should any other codec be registered with same packet class or packet id as registering codec.
   * @param codecClass Class of the codec to be registered. New instance is created automatically.
   * @returns True if the codec was registered correctly. False if registration failed
   */
  def registerCodec(codecClass: Class[_<: Codec[_<: Packet]]): Boolean = {
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