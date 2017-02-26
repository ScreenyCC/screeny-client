package screeny.protocol.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Getter;
import screeny.protocol.netty.NettyBootstrap;
import screeny.protocol.netty.PipelineFactory;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class NettyServer extends NettyBootstrap implements Runnable {
    private NettyServer instance;
    private NettyServerEventHandler nettyServerEventHandler;
    @Getter
    private int listenPort;

    public NettyServer( NettyServerEventHandler nettyServerEventHandler, int listenPort ) {
        this.instance = this;
        this.nettyServerEventHandler = nettyServerEventHandler;
        this.listenPort = listenPort;
    }

    public void start() {
        new Thread( this ).start();
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = null;
        EventLoopGroup bossGroup = null;
        try {
            SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
            SslContext sslContext = SslContextBuilder.forServer( selfSignedCertificate.certificate(), selfSignedCertificate.privateKey() ).build();


            workerGroup = new NioEventLoopGroup();
            bossGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group( workerGroup, bossGroup )
                    .channel( NioServerSocketChannel.class )
                    .childHandler( new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel( SocketChannel socketChannel ) throws Exception {
                            PipelineFactory.initServerPipeline( socketChannel, instance, sslContext );
                        }
                    } )
                    .option( ChannelOption.SO_BACKLOG, 128 )
                    .childOption( ChannelOption.SO_KEEPALIVE, true );
            ChannelFuture channelFuture = serverBootstrap.bind( this.listenPort ).sync();
            this.nettyServerEventHandler.nettyServerStartedSuccessfully( this );
            channelFuture.channel().closeFuture().sync();
            this.nettyServerEventHandler.nettyServerStopped( this );
        } catch ( InterruptedException e ) {
            this.nettyServerEventHandler.handleNettyServerException( e );
        } catch ( CertificateException e ) {
            this.nettyServerEventHandler.handleNettyServerException( e );
        } catch ( SSLException e ) {
            this.nettyServerEventHandler.handleNettyServerException( e );
        } finally {
            if ( workerGroup != null ) {
                workerGroup.shutdownGracefully();
            }
            if ( bossGroup != null ) {
                bossGroup.shutdownGracefully();
            }
        }
    }

    @Override
    public void handleException( Exception exc ) {
        this.nettyServerEventHandler.handleNettyServerException( exc );
    }

    @Override
    public void handleException( Throwable throwable ) {
        this.nettyServerEventHandler.handleNettyServerException( throwable );
    }
}
