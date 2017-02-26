package screeny.protocol.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import screeny.protocol.packet.NettyPacket;

public class NettyHandler extends SimpleChannelInboundHandler<NettyPacket> {
    private NettyBootstrap nettyBootstrap;

    public NettyHandler( NettyBootstrap nettyBootstrap ) {
        this.nettyBootstrap = nettyBootstrap;
    }

    @Override
    protected void channelRead0( ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket ) throws Exception {
        this.nettyBootstrap.submitPacket( nettyPacket );
    }

    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception {
        this.nettyBootstrap.channelActive( ctx.channel() );
    }

    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        this.nettyBootstrap.channelDisconnect( ctx.channel() );
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
        this.nettyBootstrap.handleException( cause );
        // Test
    }
}
