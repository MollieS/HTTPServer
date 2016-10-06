package httpserver.resourcemanagement;

import org.junit.Test;

import java.net.URI;
import java.nio.charset.Charset;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourceHandlerTest {

    private final String path = getClass().getClassLoader().getResource("directory").getPath();
    private final HTTPResourceHandler resourceHandler = new HTTPResourceHandler(path, new ResourceParser());

    @Test
    public void returnsADirectoryResource() {
        FileResource resource = resourceHandler.getResource(URI.create("/"));

        assertTrue(resource.isADirectory());
    }

    @Test
    public void returnsAResourceThatContainsDirectoryContent() {
        FileResource resource = resourceHandler.getResource(URI.create("/"));

        String body = new String(resource.getContents(), Charset.forName("UTF-8"));

        assertTrue(body.contains("file1"));
    }

    @Test
    public void returnsANonExistentResourceForAFileThatDoesNotExist() {
        FileResource resource = resourceHandler.getResource(URI.create("/foobar"));

        assertFalse(resource.exists());
    }
}
