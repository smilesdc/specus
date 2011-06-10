package net.tomasherman.specus.server.api.net

import packet.Packet
import org.jboss.netty.buffer.ChannelBuffer

class PacketEncoderNotFoundException(packet:Packet) extends Exception
class BufferDecoderNotFoundException(buffer:ChannelBuffer) extends Exception