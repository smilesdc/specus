package net.tomasherman.specus.server.api.net

import net.tomasherman.specus.common.api.net.Packet

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
trait CodecRepository{
  /**
   * Tries to find a suitable codec for packetId.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(packetId:Byte):Option[Codec[_<:Packet]]
  /**
   * Tries to find a suitable codec for packet class.
   * @param packetid Byte for which a suitable codec should be found.
   * @return Either None if no codec is found or Option wrapped codec.
   */
  def lookupCodec(p:Packet):Option[Codec[_<:Packet]]

  /**
   * Registers codec for lookup. Note that registration fails should any other codec be registered with same packet class or packet id as registering codec.
   * @param codecClass Class of the codec to be registered. New instance is created automatically.
   * @returns True if the codec was registered correctly. False if registration failed
   */
  def registerCodec(codecClass:Class[_<:Codec[_<:Packet]]):Boolean
}