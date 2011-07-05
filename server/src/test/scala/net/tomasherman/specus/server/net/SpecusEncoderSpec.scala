package net.tomasherman.specus.server.net

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import net.tomasherman.specus.server.api.net.packet.Packet
import net.tomasherman.specus.server.api.net.EncodingUtils._
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import org.specs2.mock.Mockito
import org.jboss.netty.handler.codec.embedder.{CodecEmbedderException, EncoderEmbedder}
import net.tomasherman.specus.server.api.net.{PacketEncoderNotFoundException, CodecRepository}

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

case class TestPacketEncoding(p1:Int,p2:String,p3:Byte) extends Packet
case class FailTestPacket() extends Packet

class TestCodec extends EncodingCodec(0x01:Byte,classOf[TestPacketEncoding]){
  protected def createChannelBuffer = ChannelBuffers.dynamicBuffer()

  protected def encodeDataToBuffer(packet: TestPacketEncoding, buffer: ChannelBuffer) {
    encodeInt(packet.p1,buffer)
    encodeString16(packet.p2,buffer)
    encodeByte(packet.p3,buffer)
  }

  def decode(buffer: ChannelBuffer) = null //irrelevant
}

class EncodingTestEnv(repo:CodecRepository) {
  val codecRepository:CodecRepository = repo
}
trait SpecusEncoderScope extends Scope with Mockito{
  val repoMock = mock[CodecRepository]
  val testPacket = new TestPacketEncoding(1337,":-)",0x66)
  repoMock.lookupCodec(testPacket) returns Some(new TestCodec)
  val env = new EncodingTestEnv(repoMock)
  val encoder = new SpecusEncoder(env)

  val expected = ChannelBuffers.dynamicBuffer()
  encodeByte(0x01,expected)
  encodeInt(1337,expected)
  encodeString16(":-)",expected)
  encodeByte(0x66,expected)
}

class SpecusEncoderSpec extends Specification{
  "SpecusEncoder" should {
    "encode protocols properly" in new SpecusEncoderScope {
      val embedder = new EncoderEmbedder[TestPacketEncoding](encoder)
      embedder.offer(testPacket)
      embedder.poll().asInstanceOf[ChannelBuffer] must_== expected
    }
    "fail when no codec found" in new SpecusEncoderScope {
      val embedder = new EncoderEmbedder[TestPacketEncoding](encoder)
      try{
        embedder.offer(new FailTestPacket)
        failure
      } catch {
        case ex:CodecEmbedderException => ex.getCause match {
          case e:PacketEncoderNotFoundException => if(e.packet.getClass == (new FailTestPacket).getClass) success; else failure
          case _ => failure
        }
        case _ => failure
      }
    }
  }
}