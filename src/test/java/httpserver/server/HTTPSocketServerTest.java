package httpserver.server;

import httpserver.ClientSocket;
import httpserver.SocketConnectionException;
import httpserver.server.HTTPSocketServer;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPSocketServerTest {

    @Test
    public void callsAcceptOnServerSocket() throws IOException {
        ServerSocketSpy serverSocketSpy = new ServerSocketSpy();
        HTTPSocketServer httpSocketServer = new HTTPSocketServer(serverSocketSpy);

        httpSocketServer.serve();

        assertEquals(1, serverSocketSpy.timesAcceptCalled);
    }

    @Test
    public void callingServeReturnsAClientSocket() throws IOException {
        ServerSocketSpy serverSocketSpy = new ServerSocketSpy();
        HTTPSocketServer httpSocketServer = new HTTPSocketServer(serverSocketSpy);

        ClientSocket socket = httpSocketServer.serve();

        assertTrue(socket != null);
    }

    @Test(expected = SocketConnectionException.class)
    public void throwsASocketConnectionException() throws IOException {
        ServerSocketThatThrowsException serverSocketThatThrowsException = new ServerSocketThatThrowsException();
        HTTPSocketServer httpSocketServer = new HTTPSocketServer(serverSocketThatThrowsException);

        httpSocketServer.serve();
    }

    @Test
    public void sendsBodyIfResponseHasBody() {

    }

    private class ServerSocketSpy extends ServerSocket {

        int timesAcceptCalled = 0;

        ServerSocketSpy() throws IOException {
        }

        @Override
        public Socket accept() {
            timesAcceptCalled++;
            return new Socket();
        }
    }

    private class ServerSocketThatThrowsException extends ServerSocket {

        ServerSocketThatThrowsException() throws IOException {
        }

        @Override
        public Socket accept() throws IOException {
            throw new IOException();
        }
    }
}