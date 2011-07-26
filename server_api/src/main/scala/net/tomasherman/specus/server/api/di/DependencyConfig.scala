package net.tomasherman.specus.server.api.di

import net.tomasherman.specus.server.api.net.CodecRepository
import net.tomasherman.specus.server.api.net.session.SessionManager
import net.tomasherman.specus.server.api.config.Configuration
import org.jboss.netty.channel.{ChannelPipelineFactory, ChannelHandler}
import net.tomasherman.specus.server.api.grid.NodeManager
import net.tomasherman.specus.server.api.plugin.definitions.PluginDefinitionLoader
import net.tomasherman.specus.server.api.plugin.{PluginEventManager, PluginManager}

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


/** Trait defining all the components required for dependency injection. */
trait DependencyConfig {
  val codecRepository: CodecRepository
  val nettyPipelineFactory: ChannelPipelineFactory
  val nodeManager: NodeManager
  val channelEncoder: ChannelHandler
  val channelDecoder: ChannelHandler
  val channelHandler: ChannelHandler
  val sessionManager: SessionManager
  val pluginManager: PluginManager
  val config: Configuration
  val pluginDefinitionLoader: PluginDefinitionLoader
  val pluginEventManager: PluginEventManager
}