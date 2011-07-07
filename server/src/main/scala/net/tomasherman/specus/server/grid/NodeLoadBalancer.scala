package net.tomasherman.specus.server.grid

import akka.actor.{Channel, Actor}
import net.tomasherman.specus.common.api.grid.messages.{Unregister, Register}
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

trait NodeLoadBalancerFunctionality {
  protected var nodeChannels = Vector[Channel[Any]]()
  var useNext = 0

  def registerChannel(ch:Channel[Any]) {
    nodeChannels = nodeChannels :+ ch
  }

  def unregisterChannel(ch:Channel[Any]) {
    nodeChannels = nodeChannels.zipWithIndex.flatMap({ p =>
      p match {
        case (chnl,i) => {
          if(chnl == ch){
            if(useNext == i) useNext = countNext(useNext,nodeChannels.size-1)
            Nil
          }else{
            List(chnl)
          }
        }
    }})
  }

  def bangNext(msg:Packet) {
    nodeChannels(useNext) ! msg
    useNext = countNext(useNext,nodeChannels.size)
  }
  
  def bangAll(msg:Packet) {
    nodeChannels.foreach({ ch => ch ! msg})
  }

  private def countNext(current:Int,size:Int) = (useNext+1) % size
}

class NodeLoadBalancer extends Actor with NodeLoadBalancerFunctionality{
  protected def receive = {
    case msg:Register => registerChannel(self.channel)
    case msg:Unregister => unregisterChannel(self.channel)
  }
}