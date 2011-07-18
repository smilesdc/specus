package net.tomasherman.specus.server

import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import java.util.concurrent.Executors
import java.net.InetSocketAddress
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

class SpecusServer(
  val port: Int,
  val env: {
    val nettyPipelineFactory: ChannelPipelineFactory
  }){
  private var running = false

  def start() {
    startNetty()
  }

  private def startNetty() {
    val bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool()))
    bootstrap.setPipeline(env.nettyPipelineFactory.getPipeline)
    bootstrap.bind(new InetSocketAddress(port))
    running = true
  }

  def isRunning = running

  def stop() = {}
}