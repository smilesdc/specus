package net.tomasherman.specus.server.api.di

import org.specs2.mutable.Specification
import org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer
import org.jboss.netty.buffer.ChannelBuffers.buffer
import net.tomasherman.specus.server.api.net.DecodingUtils._
import org.jboss.netty.util.CharsetUtil
import net.tomasherman.specus.server.api.net.DecodingErrorException
import java.nio.charset.Charset
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

class DecodingUtilsSpec extends Specification {
  "DecodingUtils" should {
    "decode byte" in {
      val b = buffer(1)
      val v = 0x7A.toByte
      b.writeByte(v)
      decodeByte(b) must_== v
    }
    "decode short" in {
      val b = buffer(2)
      val v = 0x7A7A.toShort
      b.writeShort(v)
      decodeShort(b) must_== v
    }
    "decode int" in {
      val b = buffer(4)
      val v = 0x7A7A7A7A.toInt
      b.writeInt(v)
      decodeInt(b) must_== v
    }
    "decode long" in {
      val b = buffer(8)
      val v = 0x0011223344l.toLong
      b.writeLong(v)
      decodeLong(b) must_== v
    }
    "decode flaot" in {
      val b = buffer(4)
      val v = 0x00112233f.toFloat
      b.writeFloat(v)
      decodeFloat(b) must_== v
    }
    "decode double" in {
      val b = buffer(8)
      val v = 0x00112233d.toDouble
      b.writeDouble(v)
      decodeDouble(b) must_== v
    }
    "decode Boolean" in {
      val b = buffer(3)
      b.writeByte(0x01)
      b.writeByte(0x00)
      b.writeByte(0x03)
      decodeBoolean(b) must_== true
      decodeBoolean(b) must_== false
      decodeBoolean(b) must throwA[DecodingErrorException]
    }
    "decode String 16" in {
      val v = "The cake is a lie!"
      val b = dynamicBuffer(2)
      writeString(v,b,CharsetUtil.UTF_16BE)
      decodeString16(b) must_== v
    }
    "decode String 8" in {
      val v = "You dangerous, mute lunetic!"
      val b = dynamicBuffer(2)
      writeString(v,b,CharsetUtil.UTF_8)
      decodeString8(b) must_== v
    }
    "decode multiple" in {
      val v1 = 0xAA.toByte
      val v2 = "Hello there, friend."
      val v3 = 0xCAFEBABE.toDouble
      val b = dynamicBuffer(2)
      b.writeByte(v1)
      writeString(v2,b,CharsetUtil.UTF_16BE)
      b.writeDouble(v3)
      decodeByte(b) must_== v1
      decodeString16(b) must_== v2
      decodeDouble(b) must_== v3
    }

    "decode Metadata" in {
      val v0 = 0xAA.toByte
      val v1 = 0xAAAA.toShort
      val v2 = 0xAAAAAAAA
      val v3 = 0xABCDf
      val v4 = "And now for something completely different"
      val v5 = (0xCAFE.toShort,0xAA.toByte,0xBABE.toShort)
      val v6 = (0xAA,0xBB,0xCC)
      val b = dynamicBuffer(20)
      // metadata id numbers are so weird because the id is actually being bit-shifted 5 times to the right ... therefore 4 actually needs to be 0x8whatever, or
      // 0x9(whatever), depending on what is the fifth bit of the byte
      b.writeByte(0x0F)
      b.writeByte(v0)
      b.writeByte(0x2F)
      b.writeShort(v1)
      b.writeByte(0x4F)
      b.writeInt(v2)
      b.writeByte(0x6F)
      b.writeFloat(v3)
      b.writeByte(0x8F)
      writeString(v4,b,CharsetUtil.UTF_16BE)
      b.writeByte(0xAF)
      b.writeShort(v5._1);b.writeByte(v5._2);b.writeShort(v5._3)
      b.writeByte(0xCF)
      b.writeInt(v6._1);b.writeInt(v6._2);b.writeInt(v6._3)
      b.writeByte(0x7F)
      val res = decodeMetadata(b)
      res must_== List[(Int,Any)]((0x00,v0),(0x01,v1),(0x02,v2),(0x03,v3),(0x04,v4),(0x05,v5),(0x06,v6))

    }
  }
  private def writeString(s:String,b:ChannelBuffer,c:Charset){
    val arr = s.getBytes(c)
    b.writeShort(arr.length)
    b.writeBytes(arr)
  }


}