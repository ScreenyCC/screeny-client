package screeny.protocol.netty;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import screeny.protocol.netty.client.NettyClient;

public class PipelineFactory {
    public static void initServerPipeline( SocketChannel socketChannel, NettyBootstrap nettyBootstrap, SslContext sslContext ) {
        socketChannel.pipeline().addLast( "timeout", new ReadTimeoutHandler( 30 ) );
        socketChannel.pipeline().addLast( "ssl", sslContext.newHandler( socketChannel.alloc() ) );
        socketChannel.pipeline().addLast( "splitter", new LengthFieldPrepender( 4 ) );
        socketChannel.pipeline().addLast( "prepender", new LengthFieldBasedFrameDecoder( Integer.MAX_VALUE, 0, 4, 0, 4 ) );
        socketChannel.pipeline().addLast( "decoder", new PacketDecoder( nettyBootstrap ) );
        socketChannel.pipeline().addLast( "encoder", new PacketEncoder( nettyBootstrap ) );
        socketChannel.pipeline().addLast( "handler", new NettyHandler( nettyBootstrap ) );
    }

    public static void initClientPipeline( SocketChannel socketChannel, NettyClient nettyClient, SslContext sslContext ) {
        socketChannel.pipeline().addLast( "timeout", new ReadTimeoutHandler( 30 ) );
        socketChannel.pipeline().addLast( "ssl", sslContext.newHandler( socketChannel.alloc(), nettyClient.getHost(), nettyClient.getPort() ) );
        socketChannel.pipeline().addLast( "splitter", new LengthFieldPrepender( 4 ) );
        socketChannel.pipeline().addLast( "prepender", new LengthFieldBasedFrameDecoder( Integer.MAX_VALUE, 0, 4, 0, 4 ) );
        socketChannel.pipeline().addLast( "decoder", new PacketDecoder( nettyClient ) );
        socketChannel.pipeline().addLast( "encoder", new PacketEncoder( nettyClient ) );
        socketChannel.pipeline().addLast( "handler", new NettyHandler( nettyClient ) );
    }
}
