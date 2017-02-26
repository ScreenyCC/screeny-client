package screeny.client.protocol;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import screeny.protocol.netty.NettyException;
import screeny.protocol.netty.client.NettyClient;
import screeny.protocol.netty.client.NettyClientEventHandler;

public class NettyClientConnector implements NettyClientEventHandler {
    @Setter
    private String host;
    private int port;
    private NettyClientConnectorHandler nettyClientConnectorHandler;
    @Getter
    private NettyClient nettyClient;

    public NettyClientConnector( String host, int port, NettyClientConnectorHandler nettyClientConnectorHandler ) {
        this.host = host;
        this.port = port;
        this.nettyClientConnectorHandler = nettyClientConnectorHandler;
    }

    public void connect() {
        this.nettyClient = new NettyClient( this, this.host, this.port );
        PacketRegister.registerPackets( this.nettyClient );
        this.nettyClient.start();
    }

    public void disconnect() {
        if ( this.nettyClient != null && this.nettyClient.getChannel() != null ) {
            this.nettyClient.disconnect();
        }
    }

    @Override
    public void nettyClientConnectedSuccessfully( Channel channel ) {
        nettyClientConnectorHandler.clientConnected( nettyClient );
    }

    @Override
    public void nettyClientLostConnection( NettyClient nettyClient ) {
        this.nettyClientConnectorHandler.handleException( new NettyException( "Lost connection to server" ) );
    }

    @Override
    public void nettyClientHandleException( Throwable throwable ) {
        this.nettyClientConnectorHandler.handleException( throwable );
    }
}
