package screeny.protocol.netty.client;

import io.netty.channel.Channel;

public interface NettyClientEventHandler {
    void nettyClientConnectedSuccessfully( Channel channel );

    void nettyClientLostConnection( NettyClient nettyClient );

    void nettyClientHandleException( Throwable throwable );
}
