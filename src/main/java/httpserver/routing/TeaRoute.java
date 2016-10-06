package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.ResponseMessage;

import static httpserver.httpresponse.StatusCode.OK;

public class TeaRoute extends Route {

    private static final String URI = "/tea";

    public TeaRoute(Method... methods) {
        super(URI, methods);
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        if (super.methodIsAllowed(httpRequest.getMethod())) {
            return ResponseMessage.create(OK);
        }
        return super.methodNotAllowed();
    }
}
