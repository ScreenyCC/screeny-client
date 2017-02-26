package screeny.protocol.response;

import io.netty.channel.Channel;
import lombok.Getter;
import screeny.protocol.packet.NettyPacket;
import screeny.protocol.util.InterruptableCountdownLatch;

public class NettyPacketResponse {
    @Getter
    private String responseID;
    @Getter
    private Channel channel;
    private InterruptableCountdownLatch interruptableCountdownLatch;
    @Getter
    private NettyPacket receivedPacket = null;

    public NettyPacketResponse( String responseID, Channel channel, InterruptableCountdownLatch interruptableCountdownLatch ) {
        this.responseID = responseID;
        this.channel = channel;
        this.interruptableCountdownLatch = interruptableCountdownLatch;
    }

    public void receiveAnswerPacket( NettyPacket nettyPacket ) {
        this.receivedPacket = nettyPacket;
        interruptableCountdownLatch.countDown();
    }

    public void interrupt() {
        this.interruptableCountdownLatch.interrupt();
    }
}
