package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import static httpserver.httpresponse.StatusCode.REDIRECT;

public class RedirectRoute extends Route {

    private final String baseLocation;

    public RedirectRoute(String baseLocation, Method... methods) {
        super("/redirect", methods);
        this.baseLocation = baseLocation;
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        return HTTPResponse.create(REDIRECT).withLocation(baseLocation + "/");
    }
}
