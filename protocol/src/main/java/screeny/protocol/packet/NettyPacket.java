package screeny.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import screeny.protocol.netty.NettyBootstrap;
import screeny.protocol.util.PacketUtil;

public abstract class NettyPacket {
    @Getter
    @Setter
    private Channel channel;
    @Getter
    @Setter
    private ByteBuf byteBuf;
    @Getter
    @Setter
    private NettyBootstrap nettyBootstrap;
    @Getter
    @Setter
    private String responseID = null;

    public void sendAnswer( NettyPacket nettyPacket, Runnable runnable ) {
        this.nettyBootstrap.getExecutorService().execute( new Runnable() {
            @Override
            public void run() {
                try {
                    channel.writeAndFlush( nettyPacket ).await();
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
                if ( runnable != null ) {
                    runnable.run();
                }
            }
        } );
    }

    public void sendAnswer( NettyPacket nettyPacket ) {
        sendAnswer( nettyPacket, null );
    }

    public void writeString( String string ) {
        PacketUtil.writeString( string, this.byteBuf );
    }

    public String readString() {
        return PacketUtil.readString( this.byteBuf );
    }

    public abstract void writePacket( ByteBuf byteBuf );

    public abstract void readPacket( ByteBuf byteBuf );
}
