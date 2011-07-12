package net.tomasherman.specus.server

import api.config.DefaultConfiguration
import api.di.DependencyConfig
import grid.SpecusNodeLoadBalancer
import net._
import plugin.SimplePluginManager
import session.SpecusIntSessionManager

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

class DependencyConfiguration extends DependencyConfig {
  lazy val codecRepository = new SimpleCodecRepository
  lazy val nettyPipelineFactory = new SpecusPipelineFactory(this)
  lazy val nodeLoadBalancer = new SpecusNodeLoadBalancer
  lazy val channelEncoder = new SpecusEncoder(this)
  lazy val channelDecoder = new SpecusDecoder(this)
  lazy val channelHandler = new SpecusHandler(this)
  lazy val sessionManager = new SpecusIntSessionManager
  lazy val pluginManager = new SimplePluginManager(this)
  lazy val config = new DefaultConfiguration
}