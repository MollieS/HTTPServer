package httpserver.httpresponses;

import httpserver.Resource;
import httpserver.httpresponse.HTTPResponse;
import httpserver.httpresponse.HTTPResponseWriter;
import httpserver.httpresponse.ResponseWriterException;
import httpserver.resourcemanagement.HTTPResource;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static httpserver.httpresponse.StatusCode.*;
import static httpserver.routing.Method.GET;
import static httpserver.routing.Method.PUT;
import static org.junit.Assert.assertTrue;

public class HTTPResponseWriterTest {

    private final HTTPResponseWriter httpResponseWriter = new HTTPResponseWriter(new ByteArrayOutputStream());
    private HTTPResponse okResponse = HTTPResponse.create(OK);

    @Test
    public void returnsHeaderForAResponseWithNoBody() {

        String response = new String(httpResponseWriter.parse(okResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("HTTP/1.1 200 OK\n"));
    }

    @Test
    public void returnsAHeaderForA404Response() {
        HTTPResponse httpResponse = HTTPResponse.create(NOTFOUND);

        String response = new String(httpResponseWriter.parse(httpResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("HTTP/1.1 404 Not Found\n"));
    }

    @Test(expected = ResponseWriterException.class)
    public void throwsAByteWriterExceptionIfCannotWriteHeader() {
        HTTPResponseWriter httpResponseWriter = new HTTPResponseWriter(new ByteArrayThatThrowsException());

        httpResponseWriter.parse(okResponse);
    }

    @Test
    public void addsAllowedMethodsHeader() {
        okResponse.withAllowedMethods(Arrays.asList(PUT, GET));

        String response = new String(httpResponseWriter.parse(okResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("PUT"));
    }

    @Test
    public void addsLocationHeader() {
        HTTPResponse httpResponse = HTTPResponse.create(REDIRECT);
        httpResponse.withLocation("http://localhost:9000/");

        String response = new String(httpResponseWriter.parse(httpResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("Location : http://localhost:9000/"));
    }

    @Test
    public void addsContentRangeHeader() {
        HTTPResponse httpResponse = HTTPResponse.create(PARTIAL);
        Resource resource = new HTTPResource("hello ".getBytes());
        httpResponse.withBody(resource);

        String response = new String(httpResponseWriter.parse(httpResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("Content-Range : 6"));
    }

    @Test
    public void addsDateHeader() {
        HTTPResponse httpResponse = HTTPResponse.create(PARTIAL);

        String response = new String(httpResponseWriter.parse(httpResponse), Charset.forName("UTF-8"));

        assertTrue(response.contains("Date : Wed, 5 Oct"));
    }

    private class ByteArrayThatThrowsException extends ByteArrayOutputStream {

        @Override
        public void write(byte[] bytes) throws IOException {
            throw new IOException();
        }
    }
}
