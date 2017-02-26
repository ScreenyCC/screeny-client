package screeny.protocol.packet.packets.server;

import io.netty.buffer.ByteBuf;
import screeny.protocol.packet.NettyPacket;

public class ServerPacketLoginDataResult extends NettyPacket {
    private String sessionID;

    public ServerPacketLoginDataResult() {
    }

    public ServerPacketLoginDataResult( String sessionID ) {
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

    public void setSessionID( String sessionID ) {
        this.sessionID = sessionID;
    }
}
