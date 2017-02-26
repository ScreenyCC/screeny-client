package screeny.protocol.netty;

import io.netty.channel.Channel;
import lombok.Getter;
import screeny.protocol.listener.EventHandlerMethod;
import screeny.protocol.listener.PacketHandler;
import screeny.protocol.listener.PacketListener;
import screeny.protocol.packet.NettyPacket;
import screeny.protocol.packet.PacketManager;
import screeny.protocol.response.NettyPacketResponse;
import screeny.protocol.util.InterruptableCountdownLatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class NettyBootstrap {

    private Map<Class<?>, List<EventHandlerMethod>> eventHandlers;
    private List<PacketListener> packetListenerClasses;
    @Getter
    private ExecutorService executorService;
    private PacketManager packetManager;
    private HashMap<String, NettyPacketResponse> packetResponseCallbacks = new HashMap<>();
    private HashMap<Channel, NettyPacketResponse> packetResponseChannel = new HashMap<>();

    public NettyBootstrap() {
        this.eventHandlers = new ConcurrentHashMap<>();
        this.packetListenerClasses = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newFixedThreadPool( 100 );
        this.packetManager = new PacketManager();
    }

    public NettyPacket sendAndGetResponse( Channel channel, NettyPacket nettyPacket ) throws NettyException, NettyAnswerPacketTimeout {
        if ( channel == null ) {
            throw new NettyException( "Specified channel is null" );
        }
        if ( !channel.isOpen() || !channel.isActive() || !channel.isWritable() ) {
            throw new NettyException( "Could not write in specified channel" );
        }

        UUID responseID = UUID.randomUUID();
        nettyPacket.setResponseID( responseID.toString() );

        InterruptableCountdownLatch interruptableCountdownLatch = new InterruptableCountdownLatch( 1 );
        NettyPacketResponse nettyPacketResponse = this.createNettyPacketResponse( responseID.toString(), channel, interruptableCountdownLatch );

        channel.writeAndFlush( nettyPacket );

        try {
            if ( !interruptableCountdownLatch.await( 5, TimeUnit.SECONDS ) ) {
                throw new NettyAnswerPacketTimeout( "Timeout while waiting for answer packet" );
            }
        } catch ( InterruptedException e ) {
            // NOP
        }

        removePacketResponse( nettyPacketResponse );

        if ( nettyPacketResponse.getReceivedPacket() == null ) {
            throw new NettyException( "Received answer packet is null" );
        }

        return nettyPacketResponse.getReceivedPacket();
    }

    public void addPacketListener( PacketListener packetListener ) {
        if ( !packetListenerClasses.contains( packetListener ) ) {
            packetListenerClasses.add( packetListener );
        }

        for ( Method method : packetListener.getClass().getDeclaredMethods() ) {
            PacketHandler packetHandler = method.getAnnotation( PacketHandler.class );
            if ( packetHandler != null ) {
                Class<?>[] methodParams = method.getParameterTypes();
                if ( methodParams.length == 1 ) {
                    Class<?> packetClass = methodParams[0];
                    List<EventHandlerMethod> eventHandlerMethods = eventHandlers.get( packetClass );
                    if ( eventHandlerMethods == null ) {
                        eventHandlerMethods = new ArrayList<>();
                    }
                    eventHandlerMethods.add( new EventHandlerMethod( packetListener, method ) );
                    eventHandlers.put( packetClass, eventHandlerMethods );
                }
            }
        }
    }

    public void submitPacket( NettyPacket nettyPacket ) {
        executorService.execute( () -> {
            if ( nettyPacket.getResponseID() != null ) {
                synchronized ( packetResponseCallbacks ) {
                    if ( packetResponseCallbacks.containsKey( nettyPacket.getResponseID() ) ) {
                        packetResponseCallbacks.get( nettyPacket.getResponseID() ).receiveAnswerPacket( nettyPacket );
                        packetResponseCallbacks.remove( nettyPacket.getResponseID() );
                        return;
                    }
                }
            }
            List<EventHandlerMethod> eventHandlerMethods = eventHandlers.get( nettyPacket.getClass() );
            if ( eventHandlerMethods == null ) {
                // No handler for this packet
                System.out.println( "No handler " + nettyPacket.getClass().getSimpleName() );
                return;
            }
            for ( EventHandlerMethod eventHandlerMethod : eventHandlerMethods ) {
                executorService.execute( () -> {
                    try {
                        eventHandlerMethod.getMethod().invoke( eventHandlerMethod.getListener(), nettyPacket );
                    } catch ( IllegalAccessException e ) {
                        e.printStackTrace();
                    } catch ( InvocationTargetException e ) {
                        e.printStackTrace();
                    }
                } );
            }
        } );
    }

    public void channelActive( Channel channel ) {
        executorService.execute( () -> {
            for ( PacketListener packetListener : packetListenerClasses ) {
                packetListener.channelActive( channel );
            }
        } );
    }

    public void channelDisconnect( Channel channel ) {
        executorService.execute( () -> {
            for ( PacketListener packetListener : packetListenerClasses ) {
                packetListener.channelDisconnect( channel );
            }
        } );
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public void registerPacket( Class<? extends NettyPacket> packetClass ) {
        this.packetManager.registerPacket( packetClass );
    }

    private NettyPacketResponse createNettyPacketResponse( String responseID, Channel channel, InterruptableCountdownLatch interruptableCountdownLatch ) {
        NettyPacketResponse nettyPacketResponse = new NettyPacketResponse( responseID, channel, interruptableCountdownLatch );
        synchronized ( this.packetResponseCallbacks ) {
            this.packetResponseCallbacks.put( responseID, nettyPacketResponse );
        }
        synchronized ( this.packetResponseChannel ) {
            this.packetResponseChannel.put( channel, nettyPacketResponse );
        }
        return nettyPacketResponse;
    }

    private NettyPacketResponse getNettyPacketResponse( String responseID ) {
        synchronized ( this.packetResponseCallbacks ) {
            return this.packetResponseCallbacks.get( responseID );
        }
    }

    private void removePacketResponse( NettyPacketResponse nettyPacketResponse ) {
        synchronized ( this.packetResponseCallbacks ) {
            this.packetResponseCallbacks.remove( nettyPacketResponse.getResponseID() );
        }
        synchronized ( this.packetResponseChannel ) {
            this.packetResponseChannel.remove( nettyPacketResponse.getChannel() );
        }
    }

    private void interruptPacketResponse( String responseID ) {
        NettyPacketResponse nettyPacketResponse = getNettyPacketResponse( responseID );
        removePacketResponse( nettyPacketResponse );
        nettyPacketResponse.interrupt();
    }

    public abstract void handleException( Exception exc );

    public abstract void handleException( Throwable throwable );
}
