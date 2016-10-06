package httpserver;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

public interface HTTPRouter {

    HTTPResponse route(HTTPRequest httpRequest);
}
