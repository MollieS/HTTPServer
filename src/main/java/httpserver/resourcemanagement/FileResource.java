package httpserver.resourcemanagement;

import httpserver.Resource;

import java.io.File;

import static httpserver.resourcemanagement.ResourceContentType.TEXT;

public class FileResource implements Resource {

    private final File file;
    private byte[] contents;

    public FileResource(File file) {
        this.file = file;
    }

    public boolean isADirectory() {
        return file.isDirectory();
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public boolean exists() {
        return file.exists();
    }

    public String getContentType() {
        for (ResourceContentType type : ResourceContentType.values()) {
            if (fileHasType(type)) return type.contentType;
        }
        if (file.isDirectory()) return "text/html";
        return TEXT.contentType;
    }

    private boolean fileHasType(ResourceContentType type) {
        return file.getName().contains(type.extension);
    }
}
