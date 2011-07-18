package net.tomasherman.specus.server.grid

import collection.mutable.Map
import net.tomasherman.specus.server.api.logging.Logging
import net.tomasherman.specus.server.api.grid._
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

class SpecusNodeManager extends NodeManager with Logging {

  protected val nodeMap = Map[NodeID, (Node,String)]()
  protected val nameMap = Map[String, NodeID]()
  protected var keysCache = nodeMap.keys.toIndexedSeq
  
  protected var balIndex = 0

  def registerNode(node: Node, name: String) = {
    if(nameMap.contains(name)) throw new NodeWithNameAlreadyRegisteredException(name)
    val id = IntNodeID()
    nodeMap(id) = (node,name)
    nameMap(name) = id
    updateCache()
    id
  }

  def removeNode(id: NodeID) {
    nodeMap.keys.find( _ == id ) match {
      case None => warn("Trying to remove node with non-existing id {}",id)
      case Some(x) => {
        nameMap.remove(nodeMap(x)._2)
        nodeMap.remove(x)
        updateCache()
        updateBalIndex()
      }
    }
  }

  def names = nameMap.keys.toSet

  def nodeId(name: String) = {
    nameMap.get(name)
  }

  def writeToNode(id: NodeID,msg: NodeMessage) { nodeMap(id)._1.write(msg) }

  def balancedWrite(msg: NodeMessage) {
    if(keysCache.length == 0) throw new NoNodeRegisteredException
    writeToNode(keysCache(balIndex),msg)
    updateBalIndex()
  }

  protected def updateCache() {
    keysCache = nodeMap.keys.toIndexedSeq
  }

  protected def updateBalIndex() {
    if(keysCache.length == 0) {
      0
    } else {
      balIndex = (balIndex + 1) % keysCache.length
    }
  }

}