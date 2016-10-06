package httpserver.routing;

import httpserver.ResourceHandler;
import httpserver.httprequests.HTTPRequest;
import httpserver.httpresponse.HTTPResponse;
import httpserver.resourcemanagement.HTTPResourceHandler;
import httpserver.resourcemanagement.ResourceParser;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static httpserver.routing.Method.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RouterTest {

    String path = getClass().getClassLoader().getResource("directory").getPath();
    private List<Route> routes = Arrays.asList(new CoffeeRoute(GET), new TeaRoute(GET), new MethodOptionsRoute(GET, POST, PUT, OPTIONS, HEAD), new RedirectRoute("http://localhost:5000", GET), new PartialContentRoute(new HTTPResourceHandler(path, new ResourceParser()), GET));
    ResourceHandler resourceHandler = new HTTPResourceHandler(path, new ResourceParser());
    private Router router = new Router(new FileRoute(resourceHandler), routes);

    @Test
    public void getsTheCorrectHTTPResponseForAGetToCoffee() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/coffee");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(418, httpResponse.getStatusCode());
    }

    @Test
    public void allowsGetAndHeadRequestsToAnyURI() {
        assertTrue(router.allowsMethod(GET));
        assertTrue(router.allowsMethod(HEAD));
    }

    @Test
    public void doesNotAllowPostRequestsToGeneralURLs() {
        assertFalse(router.allowsMethod(POST));
    }

    @Test
    public void returnsA418ResponseForAGETToCoffee() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/coffee");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(418, httpResponse.getStatusCode());
        assertEquals("I'm a teapot", httpResponse.getReasonPhrase());
    }

    @Test
    public void returnsA200ResponseForAGETToTea() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/tea");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void returnsA405ResponseForAPOST() {
        HTTPRequest httpRequest = new HTTPRequest(POST, "/file1");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(405, httpResponse.getStatusCode());
    }

    @Test
    public void returnsA200ResponseForOptionsRequestToMethodOptions() {
        HTTPRequest httpRequest = new HTTPRequest(OPTIONS, "/method_options");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void returnsA200ResponseForAHEADRequestToRoot() {
        HTTPRequest httpRequest = new HTTPRequest(HEAD, "/");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
    }

    @Test
    public void addsAllowedMethodsToAnOptionsRequest() {
        HTTPRequest httpRequest = new HTTPRequest(OPTIONS, "/method_options");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertNotNull(httpResponse.getAllowedMethods());
    }

    @Test
    public void buildsTheCorrectResponseForAGETRequestToRoot() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/");
        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
        assertEquals("text/html", httpResponse.getContentType());
        assertTrue(httpResponse.hasBody());
        String body = new String(httpResponse.getBody(), Charset.forName("UTF-8"));
        assertTrue(body.contains("file1"));
    }

    @Test
    public void buildsTheCorrectResponseForAGETRequestToAJPEG() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/image.jpeg");
        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
        assertEquals("image/jpeg", httpResponse.getContentType());
    }

    @Test
    public void aHeadRequestDoesNotContainBody() {
        HTTPRequest httpRequest = new HTTPRequest(HEAD, "/");
        HTTPResponse httpResponse = router.route(httpRequest);

        Assert.assertFalse(httpResponse.hasBody());
    }

    @Test
    public void returnsTheCorrectResponseForAnUnknownURL() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/foobar");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(404, httpResponse.getStatusCode());
        assertEquals("Not Found", httpResponse.getReasonPhrase());
    }

    @Test
    public void returnsTheCorrectResponseForARedirect() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/redirect");

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(302, httpResponse.getStatusCode());
        assertEquals("Found", httpResponse.getReasonPhrase());
        assertEquals("http://localhost:5000/", httpResponse.getLocation());
    }

    @Test
    public void returnsTheCorrectResponseForAPartialContentGetWithFullRange() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeEnd(0);
        httpRequest.setRangeEnd(4);

        HTTPResponse httpResponse = router.route(httpRequest);

        assertEquals(206, httpResponse.getStatusCode());
        assertEquals("Partial Content", httpResponse.getReasonPhrase());
    }

    @Test
    public void returnsTheCorrectResponseForAPartialContentGetWithRangeEnd() {
        HTTPRequest httpRequest = new HTTPRequest(GET, "/partial_content.txt");
        httpRequest.setRangeEnd(6);

        HTTPResponse httpResponse = router.route(httpRequest);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals(206, httpResponse.getStatusCode());
        assertEquals("Partial Content", httpResponse.getReasonPhrase());
        assertEquals(" 206.\n", body);
        assertEquals(6, httpResponse.getContentRange());
    }
}
