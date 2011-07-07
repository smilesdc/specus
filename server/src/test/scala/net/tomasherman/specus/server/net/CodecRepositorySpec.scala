package net.tomasherman.specus.server.net

import net.tomasherman.specus.common.api.net.Packet
import net.tomasherman.specus.server.api.net.Codec

import org.specs2.mutable._
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


class Packet1 extends Packet
class Packet2 extends Packet
class Packet3 extends Packet
class Codec1 extends Codec[Packet1](0x01,classOf[Packet1]){
  def decode(buffer: ChannelBuffer) = new Packet1
  def encode(packet: Packet1) = null
}

class Codec2 extends Codec[Packet2](0x02,classOf[Packet2]){
  def decode(buffer: ChannelBuffer) = new Packet2
  def encode(packet: Packet2) = null
}

class FailCodec extends Codec[Packet2](0x03,classOf[Packet2]){
  def decode(buffer: ChannelBuffer) = new Packet2
  def encode(packet: Packet2) = null
}

class CodecSpec extends Specification{

  def getTestRepo = {
    val repo = new SimpleCodecRepository
    repo.registerCodec(classOf[Codec1])
    repo.registerCodec(classOf[Codec2])
    repo
  }

  "CodecRepository" should {
    "lookup by id properly" in {
      val repo = getTestRepo
      val res1 = repo.lookupCodec(0x01.toByte)
      res1 must_!= None
      val instance = res1.get
      instance match {
        case x:Codec1 => {success}
        case _ => {failure("Wrong codec returned")}
      }
      val res2 = repo.lookupCodec(0x02.toByte)
      res2 must_!= None
      val instance2 = res2.get
      instance2 match {
        case x:Codec2 => {success}
        case _ => {failure("Wrong codec returned")}
      }
    }

    "lookup by packet properly" in {
      val repo = getTestRepo
      val res1 = repo.lookupCodec(new Packet1)
      res1 must_!= None
      val instance1 = res1.get
      instance1 match {
        case x:Codec1 => {success}
        case _ => {failure("Wrong codec returned")}
      }
      val res2 = repo.lookupCodec(new Packet2)
      res2 must_!= None
      val instance2 = res2.get
      instance2 match {
        case x:Codec2 => {success}
        case _ => {failure("Wrong codec returned")}
      }
    }

    "lookup should fail properly" in {
      val repo = getTestRepo
      repo.lookupCodec(new Packet3) must_== None
      repo.lookupCodec(0x03.toByte) must_== None
      repo.lookupCodec(null) must throwAn[NullPointerException]
    }

    "register should fail on null input" in {
      val repo = getTestRepo
      repo.registerCodec(null) must_== false
    }

    "register should fail when another codec is already registered with same id or packet class" in {
      val repo = getTestRepo
      repo.registerCodec(classOf[Codec1]) must_== false
      repo.registerCodec(classOf[Codec2]) must_== false

      repo.registerCodec(classOf[FailCodec]) must_== false

    }
  }

}