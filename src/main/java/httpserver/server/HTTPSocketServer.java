package httpserver.server;

import httpserver.ClientSocket;
import httpserver.SocketConnectionException;
import httpserver.SocketServer;
import httpserver.httprequests.HTTPRequestParser;
import httpserver.httpresponse.HTTPResponseWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class HTTPSocketServer implements SocketServer {

    private final ServerSocket serverSocket;

    public HTTPSocketServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public ClientSocket serve() {
        try {
            return new HTTPSocket(serverSocket.accept(), new HTTPResponseWriter(new ByteArrayOutputStream()), new HTTPRequestParser());
        } catch (IOException e) {
            throw new SocketConnectionException("Error accepting requests: ", e.getCause());
        }
    }
}
