package screeny.client.protocol;

import screeny.protocol.netty.client.NettyClient;

public interface NettyClientConnectorHandler {
    void clientConnected( NettyClient nettyClient );

    void handleException( Throwable throwable );
}
