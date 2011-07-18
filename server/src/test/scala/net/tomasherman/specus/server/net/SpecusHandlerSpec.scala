package net.tomasherman.specus.server.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.net.session.SessionManager
import net.tomasherman.specus.common.api.net.session.SessionID
import org.jboss.netty.channel._
import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.common.api.grid.messages.PacketMessage
import net.tomasherman.specus.server.api.di.DependencyConfig
import net.tomasherman.specus.server.api.grid.Node
import net.tomasherman.specus.server.grid.SpecusNodeManager

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

class SHSEnv(val sessionManager:SessionManager)

trait SpecusHandlerSpecScope extends Scope with Mockito with ThrownExpectations {
  val smgr = mock[SessionManager]
  val nodemgr = new SpecusNodeManager
  val n1 = mock[Node]
  val n2 = mock[Node]
  val name1 = "1"
  val name2 = "2"
  nodemgr.registerNode(n1,name1)
  nodemgr.registerNode(n2,name2)
  val env = mock[DependencyConfig]
  env.sessionManager returns smgr
  env.nodeManager returns nodemgr
  val handler = new SpecusHandler(env)
  val ctx = mock[ChannelHandlerContext]
  val event = mock[ChannelStateEvent]
  val chnl = mock[Channel]
  val id = mock[SessionID]
  val msgEvnt = mock[MessageEvent]
  val packet = mock[Packet]
  val packetMsg = new PacketMessage(id,packet)
  event.getChannel returns chnl
  smgr.createNewSession(chnl) returns id
}

class SpecusHandlerSpec extends Specification {
  "SpecusHandler" should {
    "connect channel properly" in new SpecusHandlerSpecScope {
      handler.channelConnected(ctx,event)
      there was one(ctx).setAttachment(id)
    }

    "disconnect channel properly" in new SpecusHandlerSpecScope {
      ctx.getAttachment returns id
      event.getChannel returns chnl
      handler.channelClosed(ctx,event)
      there was one(smgr).closeSession(id)
      there was one(chnl).close()
    }

    "receive messages properly" in new SpecusHandlerSpecScope {
      msgEvnt.getMessage returns packet
      ctx.getAttachment returns id
      val msg = new PacketMessage(id,packet)
      handler.messageReceived(ctx,msgEvnt)
      handler.messageReceived(ctx,msgEvnt)
      handler.messageReceived(ctx,msgEvnt)
      handler.messageReceived(ctx,msgEvnt)
      there was two(n1).write(msg)
      there was two(n2).write(msg)
    }
  }
}