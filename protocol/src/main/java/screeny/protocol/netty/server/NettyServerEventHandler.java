package screeny.protocol.netty.server;

public interface NettyServerEventHandler {
    void nettyServerStartedSuccessfully( NettyServer nettyServer );

    void nettyServerStopped( NettyServer nettyServer );

    void handleNettyServerException( Throwable throwable );
}
