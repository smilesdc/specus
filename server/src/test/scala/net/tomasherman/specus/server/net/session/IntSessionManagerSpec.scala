package net.tomasherman.specus.server.net.session

import org.specs2.mutable.Specification
import collection.mutable.Map
import net.tomasherman.specus.server.api.net.session.{Session, SessionID}
import org.specs2.mock.Mockito
import org.jboss.netty.channel.Channel
import net.tomasherman.specus.server.api.net.packet.Packet
import org.specs2.specification.Scope

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

class ISMImpl(mock:Map[SessionID,Session]) extends IntSessionManager {
  protected val sessions = mock
}

protected trait ISMScope extends Scope with Mockito{
  val m = mock[Map[SessionID,Session]]

  val sMock1 = mock[Session]
  val sMock2 = mock[Session]
  val sMockId1 = mock[SessionID]
  val sMockId2 = mock[SessionID]

  val pMock = mock[Packet]

  m.apply(sMockId1) returns sMock1
  m.apply(sMockId2) returns sMock2
  m.values returns List(sMock1,sMock2)
  val sessionMgr = new ISMImpl(m)
}

class IntSessionManagerSpec extends Specification with Mockito {

  "IntSessionManager" should {

    "should create session properly" in new ISMScope{
      val channel = mock[Channel]
      val sid = sessionMgr.createNewSession(channel)
      there was one(m).update(sid,new NettySession(new IntSessionID(1),channel))
    }

    "should close session properly" in new ISMScope{
      sessionMgr.closeSession(sMockId1)
      there was one(m).-(sMockId1)
      there was one(sMock1).close()

      sessionMgr.closeSession(sMockId2)
      there was one(m).-(sMockId2)
      there was one(sMock2).close()
    }

    "should broadcast properly" in new ISMScope {
      sessionMgr.broadcast(pMock)
      there was one(sMock1).write(pMock)
      there was one(sMock2).write(pMock)
    }

    "should write to session properly" in new ISMScope{
      sessionMgr.writeTo(sMockId1,pMock)
      sessionMgr.writeTo(sMockId2,pMock)

      there was one(sMock1).write(pMock)
      there was one(sMock2).write(pMock)
    }
  }
}