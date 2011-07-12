package net.tomasherman.specus.server.api.di

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

object DependencyConfigRepository extends DependencyConfigRepositoryTrait

trait DependencyConfigRepositoryTrait{
  private var _config:DependencyConfig = null
  private var lockdown = false;

  def apply() = {
    val res = config
    res
  }

  def config = {
    if(_config == null){
      throw new NoDependencyConfigurationSupplied
    } else {
      _config
    }
  }
    

  def config_= (x:DependencyConfig):Unit = {
    if(lockdown) {
      throw new ConfigAlreadyLockedDown
    }
    _config = x
    lockdown = true
  }

}