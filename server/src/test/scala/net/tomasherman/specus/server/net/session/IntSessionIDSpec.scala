package net.tomasherman.specus.server.net.session

import org.specs2.mutable.Specification
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
class IntSessionIDSpec extends Specification{
  "IntSessionID" should {
    "equal when typed as SessionID" in {
      val sid1:SessionID = new IntSessionID(1337)
      val sid2:SessionID = new IntSessionID(1337)
      sid1 must_==  sid2
    }
    "not equal when typed as SessionID" in {
      val sid1:SessionID = new IntSessionID(1337)
      val sid2:SessionID = new IntSessionID(180)
      sid1 must_!=  sid2
    }

  }

}