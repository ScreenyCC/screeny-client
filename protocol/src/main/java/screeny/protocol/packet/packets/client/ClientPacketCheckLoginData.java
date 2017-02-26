package screeny.protocol.packet.packets.client;

import io.netty.buffer.ByteBuf;
import screeny.protocol.packet.NettyPacket;

public class ClientPacketCheckLoginData extends NettyPacket {
    private String sessionID = "";
    private String username = "";
    private String password = "";

    public ClientPacketCheckLoginData() {
    }

    @Override
    public void writePacket( ByteBuf byteBuf ) {
        writeString( this.sessionID );
        writeString( this.username );
        writeString( this.password );
    }

    @Override
    public void readPacket( ByteBuf byteBuf ) {
        this.sessionID = readString();
        this.username = readString();
        this.password = readString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID( String sessionID ) {
        this.sessionID = sessionID;
    }
}
