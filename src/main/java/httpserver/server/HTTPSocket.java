package httpserver.server;

import httpserver.ClientSocket;
import httpserver.SocketConnectionException;
import httpserver.httprequests.HTTPRequest;
import httpserver.httprequests.HTTPRequestParser;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.HTTPResponseWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class HTTPSocket implements ClientSocket {

    private final Socket socket;
    private final HTTPResponseWriter httpResponseWriter;
    private final HTTPRequestParser httpRequestParser;

    public HTTPSocket(Socket socket, HTTPResponseWriter httpResponseWriter, HTTPRequestParser httpRequestParser) {
        this.socket = socket;
        this.httpResponseWriter = httpResponseWriter;
        this.httpRequestParser = httpRequestParser;
    }

    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new SocketConnectionException("Cannot close socket: ", e.getCause());
        }
    }

    @Override
    public void sendResponse(HTTPResponse httpResponse) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(httpResponseWriter.parse(httpResponse));
        } catch (IOException e) {
            throw new SocketConnectionException("Cannot get output stream: ", e.getCause());
        }
    }

    @Override
    public HTTPRequest getRequest() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[600];
            inputStream.read(buffer);
            String req = new String(buffer, Charset.forName("UTF-8"));
            return httpRequestParser.parse(req);
        } catch (IOException e) {
            throw new SocketConnectionException("Cannot get input stream: ", e.getCause());
        }
    }
}
