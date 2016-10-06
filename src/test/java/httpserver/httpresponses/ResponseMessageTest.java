package httpserver.httpresponses;

import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.StatusCode;
import httpserver.Resource;
import org.junit.Test;

import java.nio.charset.Charset;

import static httpserver.httpresponse.StatusCode.NOTALLOWED;
import static httpserver.httpresponse.StatusCode.PARTIAL;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResponseMessageTest {

    @Test
    public void buildsACoffeeResponse() {
        HTTPResponse httpResponse = HTTPResponse.create(StatusCode.TEAPOT);

        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals(418, httpResponse.getStatusCode());
        assertEquals("I'm a teapot", httpResponse.getReasonPhrase());
        assertEquals("I'm a teapot", body);
        assertEquals("text/plain", httpResponse.getContentType());
        assertNotNull(httpResponse.getOriginTime());
    }

    @Test
    public void buildsANotFoundResponse() {
        HTTPResponse httpResponse = HTTPResponse.create(StatusCode.NOTFOUND);

        assertEquals(404, httpResponse.getStatusCode());
        assertEquals("Not Found", httpResponse.getReasonPhrase());
        assertFalse(httpResponse.hasBody());
    }

    @Test
    public void buildsARedirectResponse() {
        HTTPResponse httpResponse = HTTPResponse.create(StatusCode.REDIRECT).withLocation("/");

        assertEquals(302, httpResponse.getStatusCode());
        assertEquals("Found", httpResponse.getReasonPhrase());
        assertEquals("/", httpResponse.getLocation());
    }

    @Test
    public void buildsAnOkResponseWithNoBody() {
        HTTPResponse httpResponse = HTTPResponse.create(StatusCode.OK);

        assertEquals(200, httpResponse.getStatusCode());
        assertEquals("OK", httpResponse.getReasonPhrase());
        assertFalse(httpResponse.hasBody());
    }

    @Test
    public void buildsAnOkResponseWithBody() {
        ResourceFake resourceFake = new ResourceFake();
        HTTPResponse httpResponse = HTTPResponse.create(StatusCode.OK).withBody(resourceFake);

        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals("body", body);
        assertEquals("text/plain", httpResponse.getContentType());
        assertNotNull(httpResponse.getOriginTime());
    }

    @Test
    public void buildsAPartialResponseWithBody() {
        Resource resource = new ResourceFake();
        HTTPResponse httpResponse = HTTPResponse.create(PARTIAL).withBody(resource);
        String body = new String(httpResponse.getBody(), Charset.defaultCharset());

        assertEquals(206, httpResponse.getStatusCode());
        assertEquals("Partial Content", httpResponse.getReasonPhrase());
        assertNotNull(httpResponse.getOriginTime());
        assertEquals("body", body);
        assertEquals(4, httpResponse.getContentRange());
    }

    @Test
    public void buildsAMethodNotAllowedResponse() {
        HTTPResponse httpResponse = HTTPResponse.create(NOTALLOWED);

        assertEquals(405, httpResponse.getStatusCode());
        assertEquals("Method Not Allowed", httpResponse.getReasonPhrase());
    }

    private class ResourceFake implements Resource {

        @Override
        public byte[] getContents() {
            return "body".getBytes();
        }

        @Override
        public String getContentType() {
            return "text/plain";
        }
    }
}
