package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import static httpserver.httpresponse.StatusCode.OK;

public class MethodOptionsTwoRoute extends Route {

    public MethodOptionsTwoRoute(Method... methods) {
        super("/method_options2", methods);
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        HTTPResponse httpResponse = HTTPResponse.create(OK);
        httpResponse.withAllowedMethods(super.getAllowedMethods());
        return httpResponse;
    }
}
