package httpserver.resourcemanagement;

import httpserver.Resource;

import static httpserver.resourcemanagement.ResourceContentType.TEXT;

public class HTTPResource implements Resource {

    private final byte[] contents;

    public HTTPResource(byte[] bytes) {
        this.contents = bytes;
    }

    @Override
    public byte[] getContents() {
        return contents;
    }

    @Override
    public String getContentType() {
        return TEXT.contentType;
    }
}
