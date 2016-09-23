package httpserver.httpmessages;

import httpserver.ResourceHandler;
import httpserver.ResponseBuilder;
import httpserver.resourcemanagement.Resource;
import httpserver.routing.Method;

import java.net.URI;

import static httpserver.httpmessages.HTTPResponseCode.NOTFOUND;
import static httpserver.httpmessages.HTTPResponseCode.OK;

public class HTTPResponseBuilder implements ResponseBuilder {

    private final ResourceHandler resourceHandler;

    public HTTPResponseBuilder(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    public HTTPResponse buildResponse(Method method, URI path) {
        Resource resource = resourceHandler.getResource(path);
        if (resource.exists()) {
            HTTPResponse httpResponse = new HTTPResponse(OK.code, OK.reason);
            httpResponse.setContentType(resource.getContentType());
            httpResponse.setBody(resource.getContents());
            return httpResponse;
        }
        return new HTTPResponse(NOTFOUND.code, NOTFOUND.reason);
    }
}
