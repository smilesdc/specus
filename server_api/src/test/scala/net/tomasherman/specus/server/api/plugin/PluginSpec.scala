package net.tomasherman.specus.server.api.plugin

import org.specs2.mutable.Specification
import net.tomasherman.specus.server.api.net.Codec
import net.tomasherman.specus.server.api.net.packet.Packet
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


class TestPlugin extends Plugin{
  val codecs = List(classOf[TestCodec])
}

class PluginSpec extends Specification{
  "Plugin" should {
    "correctly compile with list of classes" in {
      val p = new TestPlugin
      p.codecs.head.newInstance() must_!= new TestCodec
    }
  }
}