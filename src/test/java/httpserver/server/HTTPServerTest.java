package httpserver.server;

import httpserver.HTTPRouter;
import httpserver.Response;
import httpserver.httprequests.HTTPRequest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HTTPServerTest {

    private final ConnectionsSocketServer connectionsSocketServer = new ConnectionsSocketServer();

    @Test
    public void canAcceptConnectionsOnThePortWhenListening() throws IOException {
        HTTPServer httpServer = new HTTPServer(connectionsSocketServer, new RouterDummy());

        httpServer.start();

        assertEquals(1, connectionsSocketServer.getConnections());
    }

    private class RouterDummy implements HTTPRouter {

        @Override
        public Response route(HTTPRequest httpRequest) {
            return null;
        }
    }
}

