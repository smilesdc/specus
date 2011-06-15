package net.tomasherman.specus.server.api.net

import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.util.CharsetUtil
import java.nio.charset.Charset
import collection.mutable.ListBuffer
import annotation.tailrec

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


object DecodingUtils {

  def decodeByte(b:ChannelBuffer) = b.readByte

  def decodeShort(b:ChannelBuffer) = b.readShort

  def decodeInt(b:ChannelBuffer) = b.readInt

  def decodeLong(b:ChannelBuffer) = b.readLong

  def decodeFloat(b:ChannelBuffer) = b.readFloat

  def decodeDouble(b:ChannelBuffer) = b.readDouble

  def decodeString8(b:ChannelBuffer) = {
    decodeString(b,CharsetUtil.UTF_8)
  }

  def decodeString16(b:ChannelBuffer) = {
    decodeString(b,CharsetUtil.UTF_16BE)
  }

  private def decodeString(b:ChannelBuffer,charset:Charset) = {
    val sLen = decodeShort(b)
    val bArr = new Array[Byte](sLen)
    b.readBytes(bArr,0,sLen)
    new String(bArr,charset)
  }

  def decodeBoolean(b:ChannelBuffer) = {
    val v = b.readByte
    if(v == 0x01)
      true
    else if(v == 0x00){
      false
    }else{
      throw new DecodingErrorException("boolean",v)
    }
  }
  def decodeMetadata(b:ChannelBuffer) = {
    decodeMetadataRec(b,ListBuffer[(Int,Any)]())
  }
  @tailrec
  private def decodeMetadataRec(b:ChannelBuffer,meta:ListBuffer[(Int,Any)]):List[(Int,Any)]={
    val t = b.readByte()
    if(t == 0xff.toByte) {
      meta.toList
    }else {
      t >>> 5 match {
        case 0 => meta +=((0,decodeByte(b)))
        case 1 => meta +=((1,decodeShort(b)))
        case 2 => meta +=((2,decodeInt(b)))
        case 3 => meta +=((3,decodeFloat(b)))
        case 4 => meta +=((4,decodeString16(b)))
        case 5 => meta +=((5,(decodeShort(b),decodeByte(b),decodeShort(b))))
        case 6 => meta +=((6,(decodeInt(b),decodeInt(b),decodeInt(b))))
      }
      decodeMetadataRec(b,meta)
    }
  }

}