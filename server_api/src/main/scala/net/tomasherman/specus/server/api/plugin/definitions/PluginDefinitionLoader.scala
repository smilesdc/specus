package net.tomasherman.specus.server.api.plugin.definitions

import java.io.File

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
trait PluginDefinitionLoader {
  /** Attempts to lookup and parse plugin definitions file.
    * @param dir Directory in which the file is being looked up.
    * @param pdfName File name of plugin definitions,
    * @returns Parsed PluginDefinition instance */
  def lookupFile(dir: File,pdfName: String) = {
    dir.listFiles() find(_.getName == pdfName)
  }

  /** Takes file that contains plugin definitions and returns PluginDefinition class.
    * @param pdFile File with plugin definitions
    * @throws PluginDefinitionParsingFailed thrown if something goes wrong with parsing.
    * @return Representation of parsed data from the file. */
  def parsePluginDefinition(pdFile: File):PluginDefinition

  /** Loads Plugin definitions fro m file.
    * @param dir Directory in which the file is supposed to be.
    * @param pdfName Name of the file containing the definitions.
    * @throws PluginDefinitionParsingFailed thrown if something goes wrong with parsing.
    * @throws PluginDefinitionFileNotFound thrown when file is not found (duh!).
    * @return Representation of parsed data from the file. */
  def loadPluginFromDir(dir: File,pdfName: String) = {
    lookupFile(dir,pdfName) match {
      case None => throw new PluginDefinitionFileNotFound(pdfName,dir)
      case Some(x) => parsePluginDefinition(x)
    }
  }
}