package httpserver.routing;

import httpserver.HTTPRouter;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;

import java.net.URI;
import java.util.List;

import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.HEAD;

public class Router implements HTTPRouter {

    private final List<Route> registeredRoutes;
    private final FileRoute fileRoute;

    public Router(FileRoute fileRoute, List<Route> registeredRoutes) {
        this.registeredRoutes = registeredRoutes;
        this.fileRoute = fileRoute;
    }

    public HTTPResponse route(HTTPRequest httpRequest) {
        for (Route route : registeredRoutes) {
            if (isRegistered(httpRequest.getRequestURI(), route)) {
                return route.performAction(httpRequest);
            }
        }
        return fileRoute.performAction(httpRequest);
    }

    private boolean isRegistered(URI uri, Route route) {
        return route.getUri().equals(uri);
    }

    public boolean allowsMethod(Method method) {
        return method == GET || method == HEAD;
    }
}
