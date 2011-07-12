package net.tomasherman.specus.server.api.plugin

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

class PluginDefinitionFileNotFound(plugDefFname:String,pluginDir:File) extends Exception(String.format("File with plugin definitions - %s - not found in directory: %s.",plugDefFname,pluginDir.getCanonicalPath))
class PluginDefinitionParsingFailed(dir:File,ex:Exception) extends Exception(String.format("Parsing of file with plugin definitions failed in directory %s.",dir.getCanonicalPath),ex)