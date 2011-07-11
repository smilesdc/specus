package net.tomasherman.specus.server.grid

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import akka.actor.Channel
import org.specs2.mock._
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.common.api.grid.messages.NodeMessage

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

class NLBFImpl extends NodeLoadBalancerFunctionality{
  def getNext = useNext
  def setNext(value:Int) {useNext= value}
  def getChannels = nodeChannels
}

trait NodeLoadBalancerFuncionalityScope extends Scope with Mockito with ThrownExpectations{
  val loadBalancer = new NLBFImpl
  val msg = mock[NodeMessage]
  val channel1 = mock[Channel[Any]]
  val channel2 = mock[Channel[Any]]
  val channel3 = mock[Channel[Any]]
}

class NodeLoadBalancerFuncionalitySpec extends Specification {
  "NodeLoadBalancerFunctionality" should {
    "register channel correctly" in new NodeLoadBalancerFuncionalityScope {
      loadBalancer.getChannels.size must_== 0
      loadBalancer.registerChannel(channel1)
      loadBalancer.getChannels.size must_== 1
      loadBalancer.getChannels(0) must_== channel1
    }

    "unregister channel correctly" in new NodeLoadBalancerFuncionalityScope {
      loadBalancer.registerChannel(channel1)
      loadBalancer.registerChannel(channel2)
      loadBalancer.registerChannel(channel3)
      loadBalancer.unregisterChannel(channel2)
      loadBalancer.getChannels.contains(channel2) must_== false
    }

    "bang next channel correctly" in new NodeLoadBalancerFuncionalityScope{
      loadBalancer.registerChannel(channel1)
      loadBalancer.registerChannel(channel2)
      loadBalancer.registerChannel(channel3)
      loadBalancer.bangNext(msg)
      there was one(channel1).!(msg)
      loadBalancer.bangNext(msg)
      there was one(channel2).!(msg)
      loadBalancer.bangNext(msg)
      there was one(channel3).!(msg)
      loadBalancer.bangNext(msg)
      there was two(channel1).!(msg)
    }

    "bang all channels correctly" in new NodeLoadBalancerFuncionalityScope {
      loadBalancer.registerChannel(channel1)
      loadBalancer.registerChannel(channel2)
      loadBalancer.registerChannel(channel3)

      loadBalancer.bangAll(msg)
      there was one(channel1).!(msg)
      there was one(channel2).!(msg)
      there was one(channel3).!(msg)
    }

    "work well when nextUse channel is removed" in new NodeLoadBalancerFuncionalityScope {
      loadBalancer.registerChannel(channel1)
      loadBalancer.registerChannel(channel2)
      loadBalancer.registerChannel(channel3)

      loadBalancer.setNext(2)
      loadBalancer.unregisterChannel(channel3)
      loadBalancer.bangNext(msg)
      there was no(channel1).!(msg)
      there was one(channel2).!(msg)
      there was no(channel3).!(msg)
    }
  }
}