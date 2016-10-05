package httpserver.routing;

import httpserver.httpmessages.HTTPRequest;
import httpserver.httpmessages.HTTPResponse;
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
        HTTPResponse httpResponse  = methodOptionsTwoRoute.performAction(httpRequest);

        assertTrue(httpResponse.allowedMethods().contains(GET));
        assertTrue(httpResponse.allowedMethods().contains(OPTIONS));
        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
    }
}