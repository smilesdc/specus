package net.tomasherman.specus.server.grid

import collection.mutable.Map
import net.tomasherman.specus.server.api.grid.{Node, NodeID, NodeManager}
import net.tomasherman.specus.server.api.logging.Logging

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
    val id = IntNodeID()
    nodeMap(id) = (node,name)
    nameMap(name) = id
    updateCache()
    id
  }

  def removeNode(id: NodeID) {
    val t = nodeMap.keys.zipWithIndex.find(_._1 == id)
    t match {
      case None => warn("Trying to remove node with non-existing id {}",id)
      case Some(x) => {
        nameMap.remove(nodeMap(x._1)._2)
        nodeMap.remove(x._1)
        updateCache()
        if(x._2 == balIndex) {
          updateBalIndex()
        }
      }
    }
  }

  def names = nameMap.keys.toList

  def nodeId(name: String) = {
    nameMap.get(name)
  }

  def writeToNode(id: NodeID,msg: AnyRef) = nodeMap(id)._1.write(msg)

  def balancedWrite(msg: AnyRef) {
    val seq = nodeMap.keys.toIndexedSeq
    writeToNode(seq(balIndex),msg)
    updateBalIndex()
  }

  protected def updateCache() {
    keysCache = nodeMap.keys.toIndexedSeq
  }

  protected def updateBalIndex() {
    balIndex = balIndex + 1 % keysCache.length
  }

}