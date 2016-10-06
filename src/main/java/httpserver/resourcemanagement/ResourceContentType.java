package httpserver.resourcemanagement;

public enum ResourceContentType {

    JPEG(".jpeg", "image/jpeg"),
    PNG(".png", "image/png"),
    GIF(".gif", "image/gif"),
    TEXT(".txt", "text/plain");

    public final String extension;
    public final String contentType;

    ResourceContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }
}
