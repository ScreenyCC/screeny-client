package screeny.protocol.listener;

import io.netty.channel.Channel;

public interface PacketListener {
    void channelActive( Channel channel );

    void channelDisconnect( Channel channel );
}
