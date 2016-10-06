package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import static httpserver.httpresponse.StatusCode.TEAPOT;

public class CoffeeRoute extends Route {

    private static final String URI = "/coffee";

    public CoffeeRoute(Method... methods) {
        super(URI, methods);
    }

    public HTTPResponse performAction(HTTPRequest httpRequest) {
        if (super.methodIsAllowed(httpRequest.getMethod())) {
            return HTTPResponse.create(TEAPOT);
        }
        return super.methodNotAllowed();
    }
}
