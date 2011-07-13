package net.tomasherman.specus.server.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.server.api.net.DecodingUtils._
import net.tomasherman.specus.server.api.net.EncodingUtils._
import org.jboss.netty.buffer.{ChannelBuffer, ChannelBuffers}
import net.tomasherman.specus.server.api.net.{BufferDecoderNotFoundException, Codec, CodecRepository}
import org.jboss.netty.handler.codec.embedder.{CodecEmbedderException, DecoderEmbedder}
import org.specs2.matcher.ThrownExpectations
import net.tomasherman.specus.server.api.di.DependencyConfig

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
 * along with Specus.  If not, see <http:www.gnu.org/licenses/>.
 *
 */

case class DSTestPacket1(p1:Int,p2:String,p3:Byte) extends Packet
class TestDecodingCodec1 extends Codec[DSTestPacket1](0x01,classOf[DSTestPacket1]){
  def encode(packet: DSTestPacket1) = null //irrelevant

  def decode(buffer: ChannelBuffer) = {
    val p1 = decodeInt(buffer)
    val p2 = decodeString16(buffer)
    val p3 = decodeByte(buffer)
    new DSTestPacket1(p1,p2,p3)
  }
}
trait SpecusDecoderTestScope extends Scope with Mockito with ThrownExpectations {
  val crMock = mock[CodecRepository]
  val env = mock[DependencyConfig]
  env.codecRepository returns crMock
  val validBuffer1Bytes = ChannelBuffers.dynamicBuffer()
  encodeByte(0x01,validBuffer1Bytes)
  encodeInt(1337,validBuffer1Bytes)
  encodeString16(":-)",validBuffer1Bytes)
  encodeByte(0x00,validBuffer1Bytes)
  val validBuffer1 = ChannelBuffers.copiedBuffer(validBuffer1Bytes)
  crMock lookupCodec(0x01.toByte) returns Some(new TestDecodingCodec1)

  val invalidBufferBytes = ChannelBuffers.dynamicBuffer()
  encodeByte(0x01,invalidBufferBytes)
  encodeInt(1337,invalidBufferBytes)

  val noCodecBufferBytes = ChannelBuffers.dynamicBuffer()
  val failPacketId = 0x66:Byte
  encodeByte(failPacketId,noCodecBufferBytes)
  crMock.lookupCodec(0x66:Byte) returns None  //add None as result of 0x66 lookupCodec in repository
}

class SpecusDecoderSpec extends Specification{

  "SpecusDecoder" should {
    "decode valid buffers properly" in new SpecusDecoderTestScope{
      val decoder = new SpecusDecoder(env)
      val emb = new DecoderEmbedder[Packet](decoder)
      emb.offer(validBuffer1Bytes)
      emb.poll() must_== new DSTestPacket1(1337,":-)",0)
    }

    "decode null if buffer isn't valid" in new SpecusDecoderTestScope {
      val decoder = new SpecusDecoder(env)
      val emb = new DecoderEmbedder[Packet](decoder)
      emb.offer(invalidBufferBytes)
      emb.poll must_== null
    }

    "fail if no codec found" in new SpecusDecoderTestScope {
      val decoder = new SpecusDecoder(env)
      val emb = new DecoderEmbedder[Packet](decoder)
      try {
        emb.offer(noCodecBufferBytes)
        failure("Exception should have been thrown")
      } catch {
        case e:CodecEmbedderException => e.getCause match {
          case cause:BufferDecoderNotFoundException => cause.packetId must_== failPacketId
          case _ => failure("Something unexpected went wrong in CodecEmbedder")
        }
        case _ => failure("Incorrect type of exception cought.")
      }
    }
  }
}