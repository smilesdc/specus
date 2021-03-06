package net.tomasherman.specus.server.api.plugin.definitions

import net.tomasherman.specus.server.api.plugin.Plugin

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

trait PluginVersion {
  def canCompare(other: PluginVersion):Boolean
  protected def compare(other: PluginVersion): Option[Int]

  def >=(other: PluginVersion) = {
    safeResolve(other, _ >= 0 )
  }

  def <=(other: PluginVersion) = {
    safeResolve(other, _ <= 0)
  }

  def >(other: PluginVersion) = {
    safeResolve(other, _ > 0 )
  }

  def <(other: PluginVersion) = {
    safeResolve(other, _ < 0)
  }

  def ==(other: PluginVersion) = {
    safeResolve(other, _ == 0)
  }

  private def safeResolve(other:PluginVersion, resolve: Int => Boolean) = {
    compare(other) match {
      case None => None
      case Some(x) => Some(resolve(x))
    }
  }
}
