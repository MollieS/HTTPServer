package httpserver.httpresponse;

import httpserver.Resource;
import httpserver.Response;
import httpserver.routing.Method;

import java.util.List;

import static httpserver.httpresponse.StatusCode.TEAPOT;
import static httpserver.resourcemanagement.ResourceContentType.TEXT;

public class HTTPResponse implements Response {

    private final int statusCode;
    private final String reasonPhrase;
    private final String originTime;
    private byte[] body;
    private String contentType;
    private int contentRange;
    private String location;
    private List<Method> allowedMethods;

    private HTTPResponse(int code, String reason, HTTPResponseDate httpResponseDate) {
        this.statusCode = code;
        this.reasonPhrase = reason;
        this.originTime = httpResponseDate.getDate();
    }

    public static HTTPResponse create(StatusCode statusCode) {
        HTTPResponse httpResponse = new HTTPResponse(statusCode.code, statusCode.reason, new HTTPResponseDate());
        if (statusCode == TEAPOT) {
            httpResponse.setBody(TEAPOT.reason);
            httpResponse.setContentType(TEXT.contentType);
        }
        return httpResponse;
    }

    public HTTPResponse withAllowedMethods(List<Method> allowedMethods) {
        this.allowedMethods = allowedMethods;
        return this;
    }

    public HTTPResponse withBody(Resource resource) {
        this.body = resource.getContents();
        this.contentType = resource.getContentType();
        if (this.statusCode == 206) {
            setContentRange(body.length);
        }
        return this;
    }

    public HTTPResponse withLocation(String location) {
        setLocation(location);
        return this;
    }

    public boolean hasLocation() {
        return location != null;
    }

    public byte[] getBody() {
        return body;
    }

    public boolean hasBody() {
        return body != null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }

    public boolean hasContentRange() {
        return contentRange != 0;
    }

    public int getContentRange() {
        return contentRange;
    }

    public String getOriginTime() {
        return originTime;
    }

    public List<Method> getAllowedMethods() {
        return allowedMethods;
    }

    private void setBody(String body) {
        this.body = body.getBytes();
    }

    private void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private void setContentRange(int contentRange) {
        this.contentRange = contentRange;
    }

    private void setLocation(String location) {
        this.location = location;
    }

}
