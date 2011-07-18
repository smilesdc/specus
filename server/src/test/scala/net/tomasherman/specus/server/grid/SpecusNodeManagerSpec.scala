package net.tomasherman.specus.server.grid

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.grid.{NoNodeRegisteredException, NodeID, NodeWithNameAlreadyRegisteredException, Node}

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

class testableSNM extends SpecusNodeManager {
  def getCache = this.keysCache
  def getIdx = this.balIndex
  def setIdx(i:Int) { balIndex = i }
  def nameM = this.nameMap
  def nodeM = this.nodeMap
  def updateC = this.updateCache
}
trait SNMSScope extends Scope with Mockito with ThrownExpectations{
  val mgr = new testableSNM

  val n1 = mock[Node]
  val n2 = mock[Node]
  val n3 = mock[Node]

  val name1 = "1"
  val name2 = "2"
  val name3 = "2" //conflict

  val id1 = mock[NodeID]
  val id2 = mock[NodeID]

  def prepareMaps() {
    mgr.nodeM.+=((id1,(n1,name1)),(id2,(n2,name2)))
    mgr.nameM.+=((name1,id1),(name2,id2))
    mgr.updateC
  }
}

class SpecusNodeManagerSpec extends Specification {
  "SpecusNodeManager" should {
    "register node properly" in new SNMSScope {
      val idd1 = mgr.registerNode(n1,name1)
      mgr.nameM must_== Map((name1,idd1))
      mgr.nodeM must_== Map((idd1,(n1,name1)))

      val idd2 = mgr.registerNode(n2,name2)
      mgr.nameM must_== Map((name2,idd2),(name1,idd1))
      mgr.nodeM must_== Map((idd2,(n2,name2)),(idd1,(n1,name1)))

      mgr.registerNode(n3,name3) must throwA[NodeWithNameAlreadyRegisteredException]
    }
    "remove node properly" in new SNMSScope {
      prepareMaps()
      mgr.setIdx(1)

      mgr.removeNode(id1) //should force balIndex to be 0
      mgr.getIdx must_== 0
      mgr.nodeM must_== Map((id2,(n2,name2)))
      mgr.nameM must_== Map((name2,id2))

      mgr.removeNode(id2)
      mgr.getIdx must_== 0
      mgr.nodeM must_== Map()
      mgr.nameM must_== Map()

    }
    "return names properly" in new SNMSScope{
      prepareMaps()
      mgr.names must_== Set(name1,name2)
    }

    "return proper node id" in new SNMSScope{
      prepareMaps()
      mgr.nodeId(name1) must_== Some(id1)
      mgr.nodeId(name2) must_== Some(id2)
      mgr.nodeId("rofl ain't exist") must_== None
    }
    "write to proper node" in new SNMSScope{
      prepareMaps()
      mgr.writeToNode(id1,null)
      there was one(n1).write(null) then no(n2).write(null)
      mgr.writeToNode(id2,null)
      there was one(n1).write(null) then one(n2).write(null)
    }
    "balanced write properly" in new SNMSScope {
      mgr.balancedWrite(null) must throwA[NoNodeRegisteredException]
      prepareMaps()
      mgr.balancedWrite(null)
      mgr.balancedWrite(null)
      mgr.balancedWrite(null)
      mgr.balancedWrite(null)
      there were two(n1).write(null) then two(n2).write(null)
    }
  }

}