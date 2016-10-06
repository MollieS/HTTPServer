package httpserver.routing;

import httpserver.Resource;
import httpserver.ResourceHandler;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.FileResource;
import httpserver.resourcemanagement.HTTPResource;

import java.util.Arrays;

import static httpserver.httpresponse.StatusCode.OK;
import static httpserver.httpresponse.StatusCode.PARTIAL;

public class PartialContentRoute extends Route {

    private final ResourceHandler resourceHandler;

    public PartialContentRoute(ResourceHandler resourceHandler, Method... methods) {
        super("/partial_content.txt", methods);
        this.resourceHandler = resourceHandler;
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        FileResource resource = resourceHandler.getResource(super.getUri());
        if (httpRequest.hasRange()) {
            Resource partialResource = getPartialContent(httpRequest, resource);
            return HTTPResponse.create(PARTIAL).withBody(partialResource);
        }
        return HTTPResponse.create(OK).withBody(resource);
    }

    private HTTPResource getPartialContent(HTTPRequest httpRequest, FileResource resource) {
        int rangeStart = getRangeStart(httpRequest, resource);
        int rangeEnd = getRangeEnd(httpRequest, resource);
        byte[] body = Arrays.copyOfRange(resource.getContents(), rangeStart, rangeEnd);
        return new HTTPResource(body);
    }

    private int getRangeEnd(HTTPRequest httpRequest, FileResource resource) {
        if (httpRequest.hasRangeEnd() && httpRequest.hasRangeStart()) {
            return httpRequest.getRangeEnd();
        }
        return resource.getContents().length;
    }

    private int getRangeStart(HTTPRequest httpRequest, FileResource resource) {
        if (!httpRequest.hasRangeStart()) {
            return (resource.getContents().length - httpRequest.getRangeEnd()) + 1;
        }
        return httpRequest.getRangeStart();
    }
}
