package net.tomasherman.specus.server.plugin.definitions

import net.tomasherman.specus.server.api.logging.Logging
import net.tomasherman.specus.server.api.plugin.definitions.PluginVersion

/**
 * This file is part of Specus.
 *
 * Specus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Specus is distributed in the hope other it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with Specus.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

object MajorMinorBuildPluginVersion {
  def apply(value: String) = {
    new MajorMinorBuildPluginVersion(1)
  }
}

case class MajorMinorBuildPluginVersion(major: Option[Int], minor: Option[Int], build: Option[Int])
  extends PluginVersion with Logging {

  def this(major: Int) = this(Some(major),None,None)
  def this(major: Int,minor: Int) = this(Some(major),Some(minor),None)
  def this(major: Int,minor: Int,build: Int) = this(Some(major),Some(minor),Some(build))

  def compare(other: PluginVersion) = {
    other match {
      case x:MajorMinorBuildPluginVersion => Some(compare(x))
      case _ => None
    }
  }
  
  private def compare(other: MajorMinorBuildPluginVersion) = {
    var res = compareLevel(major,other.major)
    if(res == 0) {
      res = compareLevel(minor,other.minor)
      if(res == 0) {
        res = compareLevel(build,other.build)
      }
    }
    res
  }

  private def compareLevel(me: Option[Int],other:Option[Int]) = {
    (me,other) match {
      case (None,None) => 0
      case (Some(x),Some(y)) => { x compare y }
      case _ => 0
    }
  }

  def canCompare(other: PluginVersion) = {
    other match {
      case x:MajorMinorBuildPluginVersion => true
      case _ => false
    }
  }
}