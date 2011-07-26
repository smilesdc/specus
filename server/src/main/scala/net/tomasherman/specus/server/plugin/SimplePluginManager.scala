package net.tomasherman.specus.server.plugin

import java.io.File
import net.tomasherman.specus.server.api.config.Configuration
import net.tomasherman.specus.server.api.plugin._
import net.tomasherman.specus.server.api.plugin.definitions._
import akka.actor.Actor.actorOf
import net.tomasherman.specus.server.api.net.CodecRepository

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


/** Implementation of PluginManager. Expects to be injected with Configuration */
class SimplePluginManager(val env: {
  val config: Configuration
  val pluginDefinitionLoader: PluginDefinitionLoader
  val pluginEventManager: PluginEventManager
  val codecRepository: CodecRepository
}) extends PluginManager{
  private var eventProcessors = Map[PluginIdentifier,PluginEventProcessorId]()
  private var plugins = Map[PluginIdentifier,(PluginDefinition,Plugin)]()

  /** Loads all valid plugins from given directory.
    * @param dir Directory in which the plugins are looked up.
    * @return List of loaded plugins. */
  def bootupPlugins(dir: File) {
    val dirs = dir.listFiles().filter(_.isDirectory).toList
    plugins = (dirs.foldLeft(List[(PluginIdentifier,(PluginDefinition,Plugin))]())(
      (l,f) => {
        val pd = loadPlugin(f)
        if(l.find(_._1 == pd._1) != None) {
          throw new PluginAlreadyRegisteredException(pd._1)
        }else {
          l ++ List(pd)
        }
      }
    )).toMap
    checkPluginDependencies(plugins)
    pluginIdentifiers foreach { x=>
      registerEventHandlers(x,env.pluginEventManager)
      registerCodecs(x,env.codecRepository)
    }
  }

  def checkPluginDependencies(pluginMap:Map[PluginIdentifier,(PluginDefinition,Plugin)]) {
    pluginMap.keys.foreach({ p => //check if all the plugin dependencies are satisfied
      pluginMap(p)._1.dependencies.dep.foreach({ d =>  //check each the dependency is satisfied
        d.version.matches(pluginMap(d.identifier)._1.version) match {
          case None => throw new PluginVersionMatchingException(d.identifier,d.version,pluginMap(d.identifier)._1.version)
          case Some(x) => x match {
            case true => //versions match, can go to next iteration
            case false => throw new PluginVersionMatchingException(d.identifier,d.version,pluginMap(d.identifier)._1.version)
          }
        }
      })
    })
  }

  def plugin(ident: PluginIdentifier) = plugins(ident)._2
  def pluginDefinitions(ident: PluginIdentifier) = plugins(ident)._1
  def eventProcessorId(ident: PluginIdentifier) = eventProcessors(ident)
  def pluginIdentifiers = plugins.keySet

  def registerCodecs(ident:PluginIdentifier,codecRepository:CodecRepository) {
    plugin(ident).getCodecs match {
      case None => //done
      case Some(x) => x foreach {env.codecRepository.registerCodec(_)}
    }
  }

  def registerEventHandlers(ident:PluginIdentifier,mgr:PluginEventManager) {
    val p = plugin(ident)
    p.eventProcessorClass match {
      case None => //done
      case Some(x) => {
        val ref = actorOf(x)
        val id = env.pluginEventManager.registerEventProcessor(ref,p.registerForEvents.getOrElse(List()))
        eventProcessors = eventProcessors + ((ident,id))
      }
    }
  }


  /** Helper plugin-loading function to make bootupPlugins a little more readable.
    * Handles PluginDefinitions loading as well as instantiating of new Plugin class
    * @param f Directory in which the Plugin definitions file is expected to be.
    * @return Plugin instance. */
  def loadPlugin(f: File) = {
    val pd = env.pluginDefinitionLoader.loadPluginFromDir(f,env.config.plugin.pluginDefinitionFileName)
    (pd.identifier,(pd,instantiatePlugin(pd.pluginClass)))
  }

  /** Creates new instance of class.
    * @param String representation of the class to be created.
    * @return New instance of the desired class. */
  private def instantiatePlugin(c: String) = {
    Class.forName(c).newInstance().asInstanceOf[Plugin]
  }
}