package httpserver;

public class HTTPServer {

    private final SocketServer socketServer;
    private final int port;

    public HTTPServer(int port, SocketServer socketServer) {
        this.port = port;
        this.socketServer = socketServer;
    }

    public int getPort() {
        return port;
    }

    public void start() {
        ClientSocket socket = socketServer.serve();
        String request = socket.getRequest();
        HTTPRequest httpRequest = HTTPRequestParser.parse(request);
        HTTPResponse httpResponse = HTTPRequestHandler.handle(httpRequest);
        socket.sendResponse(httpResponse);
        socket.close();
    }
}
