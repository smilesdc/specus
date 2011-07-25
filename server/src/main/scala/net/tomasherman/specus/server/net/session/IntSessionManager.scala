package net.tomasherman.specus.server.net.session

import net.tomasherman.specus.server.api.net.session.{Session,SessionManager}
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

/** SessionManager implementation using IntSessionID implementation of
  * SessionID */
trait IntSessionManager extends SessionManager {
  protected var sessions:Map[SessionID,Session]

  def createNewSession(channel: Channel) = {
    val sid = IntSessionID()
    sessions = sessions + ((sid,new NettySession(channel)))
    sid
  }
  def closeSession(id: SessionID) {
    sessions(id).close()
    sessions - id
  }

  def broadcast(data: Packet) {
    sessions.values.foreach(s => writeToSession(s,data))
  }

  def writeTo(id: SessionID, data:Packet) {
    sessions(id).write(data)
  }

  private def writeToSession(session:Session, data:Packet) {
    session.write(data)
  }
}