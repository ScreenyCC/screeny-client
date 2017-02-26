package screeny.protocol.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import screeny.protocol.packet.NettyPacket;
import screeny.protocol.util.PacketUtil;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private NettyBootstrap nettyBootstrap;

    public PacketDecoder( NettyBootstrap nettyBootstrap ) {
        this.nettyBootstrap = nettyBootstrap;
    }

    @Override
    protected void decode( ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list ) throws Exception {
        if ( !byteBuf.isReadable() ) return;
        String packetHash = null;
        try {
            packetHash = PacketUtil.readString( byteBuf );
            Class<? extends NettyPacket> packetClass = this.nettyBootstrap.getPacketManager().getPacket( packetHash );
            if ( packetClass != null ) {
                NettyPacket nettyPacket = packetClass.newInstance();
                nettyPacket.setChannel( channelHandlerContext.channel() );
                nettyPacket.setByteBuf( byteBuf );
                nettyPacket.setNettyBootstrap( this.nettyBootstrap );
                byte read = byteBuf.readByte();
                if ( read == 1 ) {
                    nettyPacket.setResponseID( PacketUtil.readString( byteBuf ) );
                }
                nettyPacket.readPacket( byteBuf );
                list.add( nettyPacket );
            } else {
                byteBuf.skipBytes( byteBuf.readableBytes() );
                throw new NettyException( "No PacketClass found while decoding packet with hash " + packetHash );
            }
        } catch ( Exception exc ) {
            this.nettyBootstrap.handleException( exc );
        } finally {
            if ( byteBuf.readableBytes() > 0 ) {
                byteBuf.skipBytes( byteBuf.readableBytes() );
                this.nettyBootstrap.handleException( new NettyException( "Did not read all bytes while decoding " + ( packetHash != null ? "packet " + packetHash : "a unknown packet" ) ) );
            }
        }
    }
}
