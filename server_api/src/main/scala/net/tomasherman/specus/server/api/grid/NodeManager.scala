package net.tomasherman.specus.server.api.grid

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

trait NodeManager {
  def registerNode(node: Node,name: String):NodeID
  def removeNode(id: NodeID)
  def names: Set[String]
  def nodeId(name: String): Option[NodeID]
  def writeToNode(id: NodeID,msg: NodeMessage)
  def balancedWrite(msg: NodeMessage)
}
