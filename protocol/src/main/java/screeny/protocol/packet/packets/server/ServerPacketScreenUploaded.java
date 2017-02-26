package screeny.protocol.packet.packets.server;

import io.netty.buffer.ByteBuf;
import screeny.protocol.packet.NettyPacket;

public class ServerPacketScreenUploaded extends NettyPacket {
    private String url;

    public ServerPacketScreenUploaded() {
    }

    public ServerPacketScreenUploaded( String url ) {
        this.url = url;
    }

    @Override
    public void writePacket( ByteBuf byteBuf ) {
        writeString( this.url );
    }

    @Override
    public void readPacket( ByteBuf byteBuf ) {
        this.url = readString();
    }

    public String getUrl() {
        return url;
    }
}
