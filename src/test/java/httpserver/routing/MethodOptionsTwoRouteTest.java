package httpserver.routing;

import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import org.junit.Test;

import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.OPTIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MethodOptionsTwoRouteTest {

    @Test
    public void sendsTheCorrectResponseForAnOptionsRequest() {
        MethodOptionsTwoRoute methodOptionsTwoRoute = new MethodOptionsTwoRoute(GET, OPTIONS);

        HTTPRequest httpRequest = new HTTPRequest(OPTIONS, "/method_options2");
        HTTPResponse httpResponse = methodOptionsTwoRoute.performAction(httpRequest);

        assertTrue(httpResponse.getAllowedMethods().contains(GET));
        assertTrue(httpResponse.getAllowedMethods().contains(OPTIONS));
        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
    }

    @Test
    public void sendsTheCorrectResponseForAGetRequest() {
        MethodOptionsTwoRoute methodOptionsTwoRoute = new MethodOptionsTwoRoute(GET, OPTIONS);

        HTTPRequest httpRequest = new HTTPRequest(GET, "/method_options2");
        HTTPResponse httpResponse = methodOptionsTwoRoute.performAction(httpRequest);

        assertTrue(httpResponse.getAllowedMethods().contains(GET));
        assertTrue(httpResponse.getAllowedMethods().contains(OPTIONS));
        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
    }
}
