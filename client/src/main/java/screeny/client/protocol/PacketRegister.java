package screeny.client.protocol;

import screeny.protocol.netty.NettyBootstrap;
import screeny.protocol.packet.packets.client.ClientPacketCheckLoginData;
import screeny.protocol.packet.packets.client.ClientPacketLogout;
import screeny.protocol.packet.packets.client.ClientPacketUploadScreen;
import screeny.protocol.packet.packets.server.ServerPacketLoginDataResult;
import screeny.protocol.packet.packets.server.ServerPacketScreenUploaded;

public class PacketRegister {
    public static void registerPackets( NettyBootstrap nettyBootstrap ) {
        nettyBootstrap.registerPacket( ClientPacketCheckLoginData.class );
        nettyBootstrap.registerPacket( ClientPacketUploadScreen.class );
        nettyBootstrap.registerPacket( ServerPacketLoginDataResult.class );
        nettyBootstrap.registerPacket( ServerPacketScreenUploaded.class );
        nettyBootstrap.registerPacket( ClientPacketLogout.class );
    }
}
