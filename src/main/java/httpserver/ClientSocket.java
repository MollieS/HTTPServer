package httpserver;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

public interface ClientSocket {

    void close();

    void sendResponse(HTTPResponse httpResponse);

    HTTPRequest getRequest();
}
