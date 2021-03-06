package net.tomasherman.specus.common.api.grid.messages

import net.tomasherman.specus.common.api.net.session.SessionID
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

/** Abstraction of messages sent from server to node(s). */
abstract class NodeMessage

/** Message indicating that a packet was received from a client.
  * @param sid SessionID of representing client.
  * @param packet Data received from a client. */
case class PacketMessage(sid: SessionID,packet: Packet) extends NodeMessage