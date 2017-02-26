package screeny.protocol.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import screeny.protocol.packet.NettyPacket;
import screeny.protocol.util.PacketUtil;

public class PacketEncoder extends MessageToByteEncoder<NettyPacket> {
    private NettyBootstrap nettyBootstrap;

    public PacketEncoder( NettyBootstrap nettyBootstrap ) {
        this.nettyBootstrap = nettyBootstrap;
    }

    @Override
    protected void encode( ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket, ByteBuf byteBuf ) throws Exception {
        String packetHash = this.nettyBootstrap.getPacketManager().getPacketHash( nettyPacket );
        if ( packetHash != null ) {
            try {
                nettyPacket.setChannel( channelHandlerContext.channel() );
                nettyPacket.setNettyBootstrap( this.nettyBootstrap );
                nettyPacket.setByteBuf( byteBuf );
                PacketUtil.writeString( packetHash, byteBuf );
                if ( nettyPacket.getResponseID() != null ) {
                    byteBuf.writeByte( 1 );
                    PacketUtil.writeString( nettyPacket.getResponseID(), byteBuf );
                } else {
                    byteBuf.writeByte( 0 );
                }
                nettyPacket.writePacket( byteBuf );
            } catch ( Exception exc ) {
                this.nettyBootstrap.handleException( exc );
            }
        } else {
            this.nettyBootstrap.handleException( new NettyException( "No PacketHash found while encoding packet " + nettyPacket.getClass().getName() ) );
        }
    }
}
