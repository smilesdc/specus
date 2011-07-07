package net.tomasherman.specus.server.grid

import org.specs2.mutable.Specification
import net.tomasherman.specus.server.api.net.session.SessionManager
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
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

class ChWFImpl(val env:{val sessionManager:SessionManager}) extends ChannelWriterFunctionality
class ChWFEnv(val sessionManager:SessionManager)

trait ChannelWriterFunctionalityScope extends Scope with Mockito {
  val sessMgr = mock[SessionManager]
  val functionality = new ChWFImpl(new ChWFEnv(sessMgr))
  val packet = mock[Packet]
  val sid = mock[SessionID]
}

class ChannelWriterFunctionalitySpec extends Specification{
  "ChannelWriterFuncionality" should {
    "invoke writeTo correctly" in new ChannelWriterFunctionalityScope {
      functionality.writeData(sid,packet)
      there was one(sessMgr).writeTo(sid,packet)
    }
  }

}