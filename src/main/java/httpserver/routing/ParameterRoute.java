package httpserver.routing;

import httpserver.Resource;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.HTTPResource;

import static httpserver.httpresponse.StatusCode.OK;

public class ParameterRoute extends Route {

    private final String PARAM_NOTATION = "variable_";

    public ParameterRoute(Method... methods) {
        super("/parameters", methods);
    }

    @Override
    public HTTPResponse performAction(HTTPRequest httpRequest) {
        if (methodIsAllowed(httpRequest.getMethod())) {
            HTTPResponse httpResponse = HTTPResponse.create(OK);
            addBody(httpRequest, httpResponse);
            return httpResponse;
        }
        return methodNotAllowed();
    }

    private void addBody(HTTPRequest httpRequest, HTTPResponse httpResponse) {
        if (httpRequest.hasParams()) {
            Resource resource = new HTTPResource(formatParameters(httpRequest));
            httpResponse.withBody(resource);
        }
    }

    private byte[] formatParameters(HTTPRequest httpRequest) {
        String[] allParams = httpRequest.getParams().split(PARAM_NOTATION);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < allParams.length; i++) {
            stringBuilder.append(String.format("variable_%s = ", String.valueOf(i)));
            String params = allParams[i].replace(String.valueOf(i) + "=", "");
            stringBuilder.append(params);
            addMultipleParameters(stringBuilder);
        }
        return stringBuilder.toString().getBytes();
    }

    private void addMultipleParameters(StringBuilder stringBuilder) {
        if (stringBuilder.toString().endsWith("&")) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }
    }
}
