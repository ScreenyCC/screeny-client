package screeny.protocol.packet.packets.client;

import io.netty.buffer.ByteBuf;
import screeny.protocol.packet.NettyPacket;

public class ClientPacketUploadScreen extends NettyPacket {
    private byte[] screenBytes;

    public ClientPacketUploadScreen() {
    }

    public ClientPacketUploadScreen( byte[] screenBytes ) {
        this.screenBytes = screenBytes;
    }

    @Override
    public void writePacket( ByteBuf byteBuf ) {
        byteBuf.writeInt( this.screenBytes.length );
        byteBuf.writeBytes( this.screenBytes );
    }

    @Override
    public void readPacket( ByteBuf byteBuf ) {
        this.screenBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes( this.screenBytes );
    }

    public byte[] getScreenBytes() {
        return screenBytes;
    }
}
