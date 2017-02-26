package screeny.protocol.packet.packets.client;

import io.netty.buffer.ByteBuf;
import screeny.protocol.packet.NettyPacket;

public class ClientPacketLogout extends NettyPacket {
    private String sessionID;

    public ClientPacketLogout() {
    }

    public ClientPacketLogout( String sessionID ) {
        this.sessionID = sessionID;
    }

    @Override
    public void writePacket( ByteBuf byteBuf ) {
        writeString( this.sessionID );
    }

    @Override
    public void readPacket( ByteBuf byteBuf ) {
        this.sessionID = readString();
    }

    public String getSessionID() {
        return sessionID;
    }
}
