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

/** Object containing dependency configuration. Configuration has to be set
  * during start up */
object DependencyConfigRepository extends DependencyConfigRepositoryTrait

/** Functionality of DependencyConfigRepository */
trait DependencyConfigRepositoryTrait {
  private var _config: DependencyConfig = null
  private var lockdown = false;

  /** Syntactic sugar for config method.
    * @returns Current dependency configuration */
  def apply() = {
    config
  }

  /** Gives access to dependency configuration.
    * @returns Current dependency configuration */
  def config = {
    if (_config == null) {
      throw new NoDependencyConfigurationSupplied
    } else {
      _config
    }
  }

  /** Setter for dependency configuration. *THIS IS ONLY TO BE CALLED ONCE!*
    * @param config Dependency configuration.
    */
  def config_=(config: DependencyConfig) {
    if (lockdown) {
      throw new ConfigAlreadyLockedDown
    }
    _config = config
    lockdown = true
  }

}