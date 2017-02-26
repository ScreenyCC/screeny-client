package screeny.protocol.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Getter;
import screeny.protocol.netty.NettyBootstrap;
import screeny.protocol.netty.NettyException;
import screeny.protocol.netty.PipelineFactory;
import screeny.protocol.packet.NettyPacket;

public class NettyClient extends NettyBootstrap implements Runnable {
    private NettyClient instance;
    private NettyClientEventHandler nettyClientEventHandler;
    @Getter
    private String host;
    @Getter
    private int port;
    @Getter
    private Channel channel;
    private boolean disconnected = false;

    public NettyClient( NettyClientEventHandler nettyClientEventHandler, String host, int port ) {
        this.instance = this;
        this.nettyClientEventHandler = nettyClientEventHandler;
        this.host = host;
        this.port = port;
    }

    public void start() {
        new Thread( this ).start();
    }

    public void sendPacket( NettyPacket nettyPacket ) throws NettyException {
        if ( this.channel != null && this.channel.isActive() ) {
            this.getExecutorService().execute( new Runnable() {
                @Override
                public void run() {
                    channel.writeAndFlush( nettyPacket );
                }
            } );
        } else {
            throw new NettyException( "Channel is not connected" );
        }
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = null;
        try {
            SslContext sslContext = SslContextBuilder.forClient().trustManager( InsecureTrustManagerFactory.INSTANCE ).build();

            workerGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap()
                    .group( workerGroup )
                    .channel( NioSocketChannel.class )
                    .option( ChannelOption.SO_KEEPALIVE, true )
                    .handler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel socketChannel ) throws Exception {
                            PipelineFactory.initClientPipeline( socketChannel, instance, sslContext );
                        }
                    } )
                    .option( ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000 );
            ChannelFuture channelFuture = bootstrap.connect( this.host, this.port ).addListener( new ChannelFutureListener() {
                @Override
                public void operationComplete( ChannelFuture channelFuture ) throws Exception {
                }
            } ).await();

            if ( channelFuture.isSuccess() ) {
                this.channel = channelFuture.channel();
                nettyClientEventHandler.nettyClientConnectedSuccessfully( channel );
                this.channel.closeFuture().addListener( new ChannelFutureListener() {
                    @Override
                    public void operationComplete( ChannelFuture channelFuture ) throws Exception {
                        if ( !disconnected ) {
                            nettyClientEventHandler.nettyClientLostConnection( instance );
                        }
                    }
                } );
                this.channel.closeFuture().sync();
            } else {
                nettyClientEventHandler.nettyClientHandleException( new NettyException( "Could not connect to server" ) );
            }
        } catch ( Exception e ) {
            this.nettyClientEventHandler.nettyClientHandleException( e );
        } finally {
            if ( workerGroup != null ) {
                try {
                    workerGroup.shutdownGracefully().sync();
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void disconnect() {
        if ( this.channel != null ) {
            disconnected = true;
            this.channel.disconnect();
        }
    }

    @Override
    public void handleException( Exception exc ) {
        this.nettyClientEventHandler.nettyClientHandleException( exc );
    }

    @Override
    public void handleException( Throwable throwable ) {
        this.nettyClientEventHandler.nettyClientHandleException( throwable );
    }
}
