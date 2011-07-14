package net.tomasherman.specus.server.api.plugin

import org.specs2.mutable._
import net.tomasherman.specus.server.api.net.Codec
import net.tomasherman.specus.common.api.net.Packet
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
class TestPacket extends Packet
class TestCodec extends Codec[TestPacket](0x01,classOf[TestPacket]){
  def decode(buffer: ChannelBuffer) = new TestPacket
  def encode(packet: TestPacket) = null
}


class TestPlugin extends CodecPlugin{
  def getCodecs = Some(List(classOf[TestCodec]))
}

class TestPlugin2 extends CodecPlugin{
  def getCodecs = Some(List(classOf[TestCodec]))
}
class CodecPluginSpec extends Specification{
  "CodecPlugin" should {
    "correctly compile with list of classes" in {
      val p:Plugin = new TestPlugin
      p.getCodecs match {
        case None => failure("some codecs expected")
        case Some(l) => l.head.newInstance.isInstanceOf[TestCodec] must_== true
      }
    }
    "equals" in {
      new TestPlugin must_== new TestPlugin
      new TestPlugin must_!= new TestPlugin2
    }
    "hashCode" in {
      (new TestPlugin).hashCode() must_== (new TestPlugin).hashCode()
      (new TestPlugin).hashCode() must_!= (new TestPlugin2).hashCode()
    }
    "register for events and event processor class" in {
      val p:Plugin = new TestPlugin
      p.registerForEvents must_== None
      p.eventProcessorClass must_== None
    }
  }
}