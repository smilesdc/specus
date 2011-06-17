package net.tomasherman.specus.server.api.net

import org.specs2.mutable.Specification
import org.jboss.netty.buffer.ChannelBuffers.buffer
import org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer
import net.tomasherman.specus.server.api.net.EncodingUtils._
import org.jboss.netty.buffer.ChannelBuffer
import java.nio.charset.Charset
import org.jboss.netty.util.CharsetUtil

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
class EncodingUtilsSpec extends Specification{
  "Encoding utils" should {
    "encode Byte" in {
      val b = buffer(1)
      val v = 0x7F
      encodeByte(v,b)
      b.readByte() must_== v
    }
    "encode Short" in {
      val b = buffer(2)
      val v = 0x7F7F
      encodeShort(v,b)
      b.readShort() must_== v
    }
    "encode Int" in {
      val b = buffer(4)
      val v = 0xAABBCCDD
      encodeInt(v,b)
      b.readInt() must_== v
    }
    "encode Long" in {
      val b = buffer(8)
      val v = 0xAABBCCDDl
      encodeLong(v,b)
      b.readLong() must_== v
    }
    "encode Float" in {
      val b = buffer(4)
      val v = 0x00AABBCCf
      encodeFloat(v,b)
      b.readFloat() must_== v
    }
    "encode Double" in {
      val b = buffer(8)
      val v = 0x00AABBCCd
      encodeDouble(v,b)
      b.readDouble() must_== v
    }
    "encode Boolean" in {
      val b = buffer(2)
      encodeBoolean(true,b)
      encodeBoolean(false,b)
      b.readByte must_== 0x01
      b.readByte must_== 0x00
    }
    "encode String8" in {
      val b = dynamicBuffer(10)
      val v = "my goose is getting cooked"
      encodeString8(v,b)
      readString(b,CharsetUtil.UTF_8) must_== v
    }
    "encode String16" in {
      val b = dynamicBuffer(10)
      val v = "From the shadows i come!"
      encodeString16(v,b)
      readString(b,CharsetUtil.UTF_16BE) must_== v
    }
    "encode multiple" in {
      val b = dynamicBuffer(10)
      val v1 = 0xAA.toByte
      val v2 = "I'm starting to have really hard time coming up with these test strings and numbers >_<"
      val v3 = 0x00AABBCC
      encodeByte(v1,b)
      encodeString16(v2,b)
      encodeInt(v3,b)
      b.readByte() must_== v1
      readString(b,CharsetUtil.UTF_16BE) must_== v2
      b.readInt() must_== v3
    }
    "encode Metadata" in {
      val b = dynamicBuffer(10)
      val v0 = 0xFF.toByte
      val v1 = 0xFFFF.toShort
      val v2 = 0x00AABBCC
      val v3 = 0xABCDl
      val v4 = "My first loss was to a protoss"
      val v5 = (0x00AA.toShort,0xAA.toByte,0x00BB.toShort)
      val v6 = (1,2,3)
      val v = List[Any](v0,v1,v2,v3,v4,v5,v6)
      encodeMetadata(v,b)
      b.readUnsignedByte() >> 5 must_== 0x00
      b.readByte() must_== v0
      b.readUnsignedByte() >> 5 must_== 0x01
      b.readShort() must_== v1
      b.readUnsignedByte() >> 5 must_== 0x02
      b.readInt() must_== v2
      b.readUnsignedByte() >> 5 must_== 0x03
      b.readLong() must_== v3
      b.readUnsignedByte() >> 5 must_== 0x04
      readString(b,CharsetUtil.UTF_16BE) must_== v4
      b.readUnsignedByte() >> 5 must_== 0x05
      (b.readShort(),b.readByte(), b.readShort()) must_== v5
      b.readUnsignedByte() >> 5 must_== 0x06
      (b.readInt(),b.readInt(),b.readInt()) must_== v6
    }
    
  }

  def readString(b:ChannelBuffer,c:Charset)={
    val len = b.readShort
    val arr = new Array[Byte](len)
    b.readBytes(arr)
    new String(arr,c)
  }
}