package httpserver.routing;

import httpserver.ResourceHandler;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.HTTPResponseDate;
import httpserver.resourcemanagement.Resource;

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
        Resource resource = resourceHandler.getResource(super.getUri());
        if (httpRequest.hasRange()) {
            return getPartialContent(httpRequest, resource);
        }
        return getOkResponse(resource);
    }

    private HTTPResponse getOkResponse(Resource resource) {
        HTTPResponse httpResponse = new HTTPResponse(OK.code, OK.reason, new HTTPResponseDate());
        httpResponse.setBody(resource.getContents());
        httpResponse.setContentType(resource.getContentType());
        return httpResponse;
    }

    private HTTPResponse getPartialContent(HTTPRequest httpRequest, Resource resource) {
        int rangeStart = getRangeStart(httpRequest, resource);
        int rangeEnd = getRangeEnd(httpRequest, resource);
        byte[] body = Arrays.copyOfRange(resource.getContents(), rangeStart, rangeEnd);
        return getPartialResponse(resource, body);
    }

    private int getRangeEnd(HTTPRequest httpRequest, Resource resource) {
        if (httpRequest.hasRangeEnd() && httpRequest.hasRangeStart()) {
            return httpRequest.getRangeEnd();
        }
        return resource.getContents().length;
    }

    private int getRangeStart(HTTPRequest httpRequest, Resource resource) {
        if (!httpRequest.hasRangeStart()) {
            return (resource.getContents().length - httpRequest.getRangeEnd()) + 1;
        }
        return httpRequest.getRangeStart();
    }

    private HTTPResponse getPartialResponse(Resource resource, byte[] body) {
        HTTPResponse httpResponse = new HTTPResponse(PARTIAL.code, PARTIAL.reason, new HTTPResponseDate());
        httpResponse.setBody(body);
        httpResponse.setContentType(resource.getContentType());
        httpResponse.setContentRange(body.length);
        return httpResponse;
    }
}
