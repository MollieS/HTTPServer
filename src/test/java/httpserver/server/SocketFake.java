package httpserver.server;

import httpserver.ClientSocket;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import static httpserver.routing.Method.GET;

public class SocketFake implements ClientSocket {

    @Override
    public void close() {

    }

    @Override
    public void sendResponse(HTTPResponse httpResponse) {

    }

    @Override
    public HTTPRequest getRequest() {
        return new HTTPRequest(GET, "/");
    }
}
