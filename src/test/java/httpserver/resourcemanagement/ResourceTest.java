package httpserver.resourcemanagement;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ResourceTest {

    private final String path = getClass().getClassLoader().getResource("directory").getPath();

    @Test
    public void knowsIfItExists() {
        File file = new File("/does/not/exist/");

        FileResource resource = new FileResource(file);

        assertFalse(resource.exists());
    }

    @Test
    public void hasAContentTypeForATextFile() {
        File file = new File(path + "/file1");

        FileResource resource = new FileResource(file);

        assertEquals("text/plain", resource.getContentType());
    }

    @Test
    public void hasAContentTypeForAJPEGFile() {
        File file = new File(path + "image.jpeg");

        FileResource resource = new FileResource(file);

        assertEquals("image/jpeg", resource.getContentType());
    }

    @Test
    public void hasAContentTypeForAPNGFile() {
        File file = new File(path + "image.png");

        FileResource resource = new FileResource(file);

        assertEquals("image/png", resource.getContentType());
    }

    @Test
    public void hasAContentTypeForAGIF() {
        File file = new File(path + "image.gif");

        FileResource resource = new FileResource(file);

        assertEquals("image/gif", resource.getContentType());
    }

    @Test
    public void hasCorrectContentTypeForDirectory() {
        File file = new File(path);

        FileResource resource = new FileResource(file);

        assertEquals("text/html", resource.getContentType());
    }
}
