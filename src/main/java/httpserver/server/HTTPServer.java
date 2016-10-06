package httpserver.server;

import httpserver.ClientSocket;
import httpserver.HTTPRouter;
import httpserver.SocketServer;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

public class HTTPServer {

    private final SocketServer socketServer;
    private final HTTPRouter router;

    public HTTPServer(SocketServer socketServer, HTTPRouter router) {
        this.socketServer = socketServer;
        this.router = router;
    }

    public void start() {
        ClientSocket socket = socketServer.serve();
        HTTPRequest httpRequest = socket.getRequest();
        HTTPResponse httpResponse = router.route(httpRequest);
        socket.sendResponse(httpResponse);
        socket.close();
    }
}
