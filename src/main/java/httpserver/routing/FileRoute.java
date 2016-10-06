package httpserver.routing;

import httpserver.ResourceHandler;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.FileResource;

import static httpserver.httpresponse.StatusCode.*;
import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.HEAD;

public class FileRoute extends Route {

    private final ResourceHandler resourceHandler;

    public FileRoute(ResourceHandler resourceHandler) {
        super("/", GET, HEAD);
        this.resourceHandler = resourceHandler;
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        if (!methodIsAllowed(httpRequest.getMethod())) {
            return HTTPResponse.create(NOTALLOWED);
        }
        FileResource resource = resourceHandler.getResource(httpRequest.getRequestURI());
        if (resource.exists()) {
            HTTPResponse responseMessage = HTTPResponse.create(OK);
            if (httpRequest.getMethod() == GET) {
                return responseMessage.withBody(resource);
            }
            return responseMessage;
        }
        return HTTPResponse.create(NOTFOUND);
    }
}
