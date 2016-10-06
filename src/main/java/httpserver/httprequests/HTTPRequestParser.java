package httpserver.httprequests;

import httpserver.routing.Method;

import java.net.URI;

public class HTTPRequestParser {

    public HTTPRequest parse(String request) {
        String[] requestElements = request.split(" ");
        Method requestMethod = getMethod(requestElements[0]);
        URI uri = URI.create(requestElements[1]);
        return createRequest(request, uri, requestMethod);
    }

    private HTTPRequest createRequest(String request, URI uri, Method requestMethod) {
        HTTPRequest httpRequest = new HTTPRequest(requestMethod, uri.getPath());
        addRange(request, httpRequest);
        addData(request, httpRequest);
        addQuery(uri, httpRequest);
        return httpRequest;
    }

    private void addRange(String request, HTTPRequest httpRequest) {
        String[] lines = request.split("\n");
        if (request.contains("Range")) {
            String rangeLine = getRangeLine(lines);
            String[] range = rangeLine.split("=")[1].split("-");
            setRangeStart(httpRequest, range[0]);
            setRangeEnd(httpRequest, range);
        }
    }

    private void setRangeEnd(HTTPRequest httpRequest, String[] range) {
        if (range.length > 1 && !(range[1].trim().equals(""))) {
            httpRequest.setRangeEnd(Integer.valueOf(range[1].trim()));
        }
    }

    private void setRangeStart(HTTPRequest httpRequest, String s) {
        if (!s.equals("")) {
            httpRequest.setRangeStart(Integer.valueOf(s));
        }
    }

    private String getRangeLine(String[] lines) {
        String rangeLine = null;
        for (String line : lines) {
            if (line.contains("Range")) {
                rangeLine = line;
            }
        }
        return rangeLine;
    }

    private void addQuery(URI uri, HTTPRequest httpRequest) {
        if (uri.getQuery() != null) {
            httpRequest.setParams(uri.getQuery());
        }
    }

    private void addData(String request, HTTPRequest httpRequest) {
        if (request.contains("data=")) {
            parseData(request, httpRequest);
        }
    }

    private void parseData(String request, HTTPRequest httpRequest) {
        String[] lines = request.split("\n");
        String lastLine = lines[lines.length - 1];
        httpRequest.setData(lastLine.trim());
    }

    private Method getMethod(String requestMethod) {
        for (Method method : Method.values()) {
            if (methodIsValid(requestMethod, method)) {
                return method;
            }
        }
        return Method.BOGUS;
    }

    private boolean methodIsValid(String requestMethod, Method method) {
        return method.name().equals(requestMethod);
    }
}
