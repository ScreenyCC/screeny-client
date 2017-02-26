package screeny.client.process;

import screeny.client.ScreenyClient;
import screeny.client.config.ConnectionConfig;
import screeny.client.protocol.NettyClientConnector;
import screeny.client.protocol.NettyClientConnectorHandler;
import screeny.protocol.netty.NettyAnswerPacketTimeout;
import screeny.protocol.netty.NettyException;
import screeny.protocol.netty.client.NettyClient;

public class LoginDataCheckerStandalone extends LoginDataChecker implements NettyClientConnectorHandler {
    private NettyClientConnector nettyClientConnector;
    private LoginDataCheckerResponse loginDataCheckerResponse;

    public LoginDataCheckerStandalone( String sessionID, String username, String passowrd, LoginDataCheckerResponse loginDataCheckerResponse ) {
        super( sessionID, username, passowrd );
        this.loginDataCheckerResponse = loginDataCheckerResponse;
        this.nettyClientConnector = new NettyClientConnector( ScreenyClient.getClientSettingsStore().getServerAddress(), ConnectionConfig.getPORT(), this );
    }

    public void check() {
        this.nettyClientConnector.connect();
    }

    @Override
    public void clientConnected( NettyClient nettyClient ) {
        // Send and get response
        ScreenyClient.getExecutor().execute( () -> {
            try {
                check( this.nettyClientConnector.getNettyClient() );
                this.nettyClientConnector.disconnect();
                this.loginDataCheckerResponse.success();
            } catch ( NettyAnswerPacketTimeout nettyAnswerPacketTimeout ) {
                handleException( nettyAnswerPacketTimeout );
            } catch ( NettyException e ) {
                handleException( e );
            } catch ( LoginDataCheckerException e ) {
                handleException( e );
            }
        } );
    }

    @Override
    public void handleException( Throwable throwable ) {
        this.nettyClientConnector.disconnect();
        this.loginDataCheckerResponse.exception( throwable );
    }

    public interface LoginDataCheckerResponse {
        void success();

        void exception( Throwable throwable );
    }
}
