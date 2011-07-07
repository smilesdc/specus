package net.tomasherman.specus.server.net.session

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import org.jboss.netty.channel.Channel
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

trait SessionTestScope extends Scope with Mockito{
  val session = mock[NettySession]
  val channel = mock[Channel]
  val packet = mock[Packet]
  session.channel returns channel
}

class NettySessionSpec extends Specification{
  "NettySession" should {
    "call close on channel properly" in new SessionTestScope {
      session.close()
      there was one(channel).close()
    }

    "call write on channel properly" in new SessionTestScope {
      session.write(packet)
      there was one(channel).write(packet)
    }
  }
}