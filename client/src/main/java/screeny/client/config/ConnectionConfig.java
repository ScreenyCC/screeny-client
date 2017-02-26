package screeny.client.config;

import lombok.Getter;
import lombok.Setter;

public class ConnectionConfig {
    @Getter
    @Setter
    private static String HOST = "socket.screeny.cc";
    @Getter
    private static int PORT = 5412;
}
