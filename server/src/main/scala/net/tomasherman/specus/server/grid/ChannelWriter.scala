package net.tomasherman.specus.server.grid

import net.tomasherman.specus.server.api.net.session.SessionManager
import net.tomasherman.specus.common.api.net.session.SessionID
import net.tomasherman.specus.common.api.net.Packet
import akka.actor.Actor
import net.tomasherman.specus.common.api.grid.messages.WriteRequest

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

trait ChannelWriterFunctionality {
  val env:{val sessionManager:SessionManager}

  def writeData(sessId:SessionID,packet:Packet) {
    env.sessionManager.writeTo(sessId,packet)
  }
}

class ChannelWriter(val env:{val sessionManager:SessionManager}) extends Actor with ChannelWriterFunctionality{
  protected def receive = {
    case WriteRequest(sessId,data) => writeData(sessId,data)
  }
}