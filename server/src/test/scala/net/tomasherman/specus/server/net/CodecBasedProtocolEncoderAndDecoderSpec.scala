package net.tomasherman.specus.server.net

import org.specs2.mutable.Specification
import org.specs2.mock._
import net.tomasherman.specus.server.api.net.packet.Packet
import org.jboss.netty.buffer.ChannelBuffers
import net.tomasherman.specus.server.api.net.{BufferDecoderNotFoundException, PacketEncoderNotFoundException, Codec, CodecRepository}

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

case class TestPacket1() extends Packet
case class TestPacket2() extends Packet
case class TestPacket3() extends Packet

object TestEnv extends Mockito{
  val codecRepository:CodecRepository = {
    val repMock = mock[CodecRepository]
    val codec1 = mock[Codec[TestPacket1]]
    codec1.encode(new TestPacket1()) returns ChannelBuffers.copiedBuffer(Array[Byte](0x00,0x01))
    codec1.decode(ChannelBuffers.copiedBuffer(Array[Byte](0x01))) returns new TestPacket1()
    val codec2 = mock[Codec[TestPacket2]]
    codec2.encode(new TestPacket2()) returns ChannelBuffers.copiedBuffer(Array[Byte](0x01,0x02))
    codec2.decode(ChannelBuffers.copiedBuffer(Array[Byte](0x02))) returns new TestPacket2()
    repMock.lookupCodec(new TestPacket1()) returns Some(codec1)
    repMock.lookupCodec(0x00.toByte) returns Some(codec1)
    repMock.lookupCodec(new TestPacket2()) returns Some(codec2)
    repMock.lookupCodec(0x01.toByte) returns Some(codec2)
    repMock.lookupCodec(new TestPacket3()) returns None
    repMock.lookupCodec(0x03.toByte) returns None

    repMock
  }
}

class Encoder(val env:{val codecRepository:CodecRepository}) extends CodecBasedProtocolEncoder
class Decoder(val env:{val codecRepository:CodecRepository}) extends CodecBasedProtocolDecoder

class CodecBasedProtocolEncoderSpec extends Specification{
  val encoder = new Encoder(TestEnv)
  "CodecBasedProtocolEncoder" should {

    "should encode protocol according to the codecs" in {
      encoder.encode(new TestPacket1()).array.toList must_== List[Byte](0x00,0x01)
      encoder.encode(new TestPacket2()).array.toList must_== List[Byte](0x01,0x02)
    }
    "should fail properly" in {
      encoder.encode(new TestPacket3()) must throwAn[PacketEncoderNotFoundException]
    }
  }
  "CodecBasedProtocolDecoder" should  {
  val decoder = new Decoder(TestEnv)
    "should decode protocol according to the codecs" in {
      decoder.decode(ChannelBuffers.copiedBuffer(Array[Byte](0x00,0x01))) must_==  new TestPacket1()
      decoder.decode(ChannelBuffers.copiedBuffer(Array[Byte](0x01,0x02))) must_==  new TestPacket2()
    }
    "should fail properly" in {
      decoder.decode(ChannelBuffers.copiedBuffer(Array[Byte](0x03))) must throwAn[BufferDecoderNotFoundException]
    }
  }
}