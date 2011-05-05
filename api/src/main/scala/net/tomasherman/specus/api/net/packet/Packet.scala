package net.tomasherman.specus.api.net.packet

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

/**
 * Parent class of all packet case classes.
 * @param id Identifier of packet. Must be unique in the whole build
 */
abstract class Packet(id:Byte)

case class KeepAlive extends Packet(0x00)
case class LoginRequest(protocolVersion:Int,username:String,mapSeed:Long,dimension:Byte) extends Packet(0x01)
case class Handshake(content:String) extends Packet(0x02)
case class ChatMsg(content:String) extends Packet(0x03)