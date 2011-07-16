package net.tomasherman.specus.server.api.net.session

import org.jboss.netty.channel.Channel
import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.common.api.net.session.SessionID

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

/** Abstraction of a Session container. It creates sessions from Channels and
  * hides them behind SessionIDs. */
trait SessionManager {
  
  /** Creates a Session and hides it behind SessionID
    * @param channel Channel into which the created session will write.
    * @return SessionID of the created session */
  def createNewSession(channel: Channel): SessionID

  /** Writes data into Session
    * @param id Id of session into which will be written.
    * @param data Data to be written */
  def writeTo(id: SessionID, data: Packet)

  /** Writes data to all the opened sessions.
    * @param data Data to be written.
    */
  def broadcast(data: Packet)

  /** Closes the session.
    * @param id Id of the session to be closed.
    */
  def closeSession(id: SessionID)
}