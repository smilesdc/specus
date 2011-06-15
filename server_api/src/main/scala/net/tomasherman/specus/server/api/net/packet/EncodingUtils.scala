package net.tomasherman.specus.server.api.net.packet

import java.lang.Byte
import org.jboss.netty.buffer.ChannelBuffer

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


object EncodingUtils {
  def encodeByte(v:Byte,b:ChannelBuffer){}
  def encodeShort(v:Short,b:ChannelBuffer){}
  def encodeInt(v:Int,b:ChannelBuffer){}
  def encodeLong(v:Long,b:ChannelBuffer){}
  def encodeFloat(v:Float,b:ChannelBuffer){}
  def encodeDouble(v:Double,b:ChannelBuffer){}
  def encodeString8(v:String,b:ChannelBuffer){}
  def encodeString16(v:String,b:ChannelBuffer){}
  private def encodeString(v:String,b:ChannelBuffer){}
  def encodeMetadata(v:List[(Int,Any)],b:ChannelBuffer){}
}