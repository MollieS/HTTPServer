package httpserver.server;

import httpserver.SocketConnectionException;
import httpserver.httprequests.HTTPRequest;
import httpserver.httprequests.HTTPRequestParser;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.HTTPResponseWriter;
import httpserver.httpresponses.ResponseDateFake;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static httpserver.routing.Method.GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HTTPSocketTest {

    private final SocketFake socketFake = new SocketFake();
    private final SocketThatThrowsException socketThatThrowsException = new SocketThatThrowsException();
    private HTTPResponse OkResponse = new HTTPResponse(200, "OK", new ResponseDateFake());

    @Test
    public void closesTheSocket() {
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.close();

        assertTrue(socketFake.isClosed);
    }

    @Test(expected = SocketConnectionException.class)
    public void throwsASocketConnectionExceptionIfErrorClosing() {
        HTTPSocket httpSocket = createSocket(socketThatThrowsException);

        httpSocket.close();
    }

    @Test
    public void getsTheOutputStreamFromTheSocket() {
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.sendResponse(OkResponse);

        assertTrue(socketFake.getOutputStreamHasBeenCalled);
    }

    @Test
    public void writesTheHTTPResponseToTheSocketOutputStream() {
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.sendResponse(OkResponse);
        OutputStream outputStream = socketFake.getOutputStream();

        assertTrue(outputStream.toString().contains("HTTP/1.1 200 OK\n"));
    }

    @Test(expected = SocketConnectionException.class)
    public void throwsASocketConnectionExceptionIfOutputStreamCannotBeRetrieved() {
        HTTPSocket httpSocket = createSocket(socketThatThrowsException);

        httpSocket.sendResponse(OkResponse);
    }

    @Test
    public void getsTheInputStreamFromTheSocket() {
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.getRequest();

        assertTrue(socketFake.getInputStreamHasBeenCalled);
    }

    @Test
    public void getsTheRequestFromInputStream() {
        HTTPSocket httpSocket = createSocket(socketFake);

        HTTPRequest request = httpSocket.getRequest();

        assertEquals(GET, request.getMethod());
    }

    @Test(expected = SocketConnectionException.class)
    public void throwsASocketConnectionExceptionIfInputStreamCannotBeRetrieved() {
        HTTPSocket httpSocket = createSocket(socketThatThrowsException);

        httpSocket.getRequest();
    }

    @Test
    public void sendsTheBodyOfTheResponseIfThereIsBody() {
        OkResponse.setBody("This is the body".getBytes());
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.sendResponse(OkResponse);
        OutputStream outputStream = socketFake.getOutputStream();

        assertTrue(outputStream.toString().contains("This is the body"));
    }

    @Test
    public void sendsTheLocationHeader() {
        HTTPResponse httpResponse = new HTTPResponse(302, "Found", new ResponseDateFake());
        httpResponse.setLocation("http://localhost:8080/");
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.sendResponse(httpResponse);
        OutputStream outputStream = socketFake.getOutputStream();

        assertTrue(outputStream.toString().contains("Location : http://localhost:8080/"));
    }

    @Test
    public void setsTheContentRangeHeader() {
        HTTPResponse httpResponse = new HTTPResponse(206, "Partial Content", new ResponseDateFake());
        httpResponse.setContentRange(6);
        HTTPSocket httpSocket = createSocket(socketFake);

        httpSocket.sendResponse(httpResponse);
        OutputStream outputStream = socketFake.getOutputStream();

        assertTrue(outputStream.toString().contains("Content-Range : 6"));
    }

    private HTTPSocket createSocket(Socket socket) {
        return new HTTPSocket(socket, new HTTPResponseWriter(new ByteArrayOutputStream()), new HTTPRequestParser());
    }

    private class SocketFake extends Socket {

        boolean isClosed = false;
        boolean getOutputStreamHasBeenCalled = false;
        boolean getInputStreamHasBeenCalled = false;
        private final OutputStream outputStream = new ByteArrayOutputStream();

        @Override
        public void close() {
            isClosed = true;
        }

        @Override
        public OutputStream getOutputStream() {
            getOutputStreamHasBeenCalled = true;
            return outputStream;
        }

        @Override
        public InputStream getInputStream() {
            getInputStreamHasBeenCalled = true;
            return new ByteArrayInputStream(("GET / HTTP/1.1").getBytes());
        }
    }

    private class SocketThatThrowsException extends Socket {

        @Override
        public void close() throws IOException {
            throw new IOException();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new IOException();
        }
    }
}
