package screeny.protocol.packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PacketManager {
    private Map<String, Class<? extends NettyPacket>> registeredPacketsByHash = new ConcurrentHashMap<>();
    private Map<Class<? extends NettyPacket>, String> packetHashsByPacketClass = new ConcurrentHashMap<>();

    public void registerPacket( Class<? extends NettyPacket> nettyPacket ) {
        String packetHash = null;
        try {
            packetHash = nettyPacket.newInstance().getClass().getSimpleName();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
        }

        if ( packetHash == null ) {
            return;
        }

        registeredPacketsByHash.put( packetHash, nettyPacket );
        packetHashsByPacketClass.put( nettyPacket, packetHash );
    }

    public Class<? extends NettyPacket> getPacket( String packetHash ) {
        return registeredPacketsByHash.get( packetHash );

    }

    public String getPacketHash( NettyPacket nettyPacket ) {
        if ( packetHashsByPacketClass.containsKey( nettyPacket.getClass() ) ) {
            return packetHashsByPacketClass.get( nettyPacket.getClass() );
        }
        return null;
    }
}
