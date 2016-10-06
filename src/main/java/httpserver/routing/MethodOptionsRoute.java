package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import static httpserver.httpresponse.StatusCode.OK;

public class MethodOptionsRoute extends Route {

    public MethodOptionsRoute(Method... methods) {
        super("/method_options", methods);
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        HTTPResponse httpResponse;
        if (methodIsAllowed(httpRequest.getMethod())) {
            httpResponse = HTTPResponse.create(OK);
            if (httpRequest.getMethod() == Method.OPTIONS) {
                httpResponse.withAllowedMethods(super.getAllowedMethods());
            }
            return httpResponse;
        }
        return super.methodNotAllowed();
    }
}
