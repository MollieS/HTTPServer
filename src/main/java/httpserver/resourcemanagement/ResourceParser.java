package httpserver.resourcemanagement;

import java.io.*;
import java.nio.file.Files;

public class ResourceParser {

    public byte[] parse(File directory) {
        return getFiles(directory);
    }

    private byte[] getFiles(File file) {
        if (file.isDirectory()) {
            return returnFiles(file);
        }
        return readFile(file);
    }

    private byte[] readFile(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] returnFiles(File directory) {
        String files = "";
        for (File fileEntry : directory.listFiles()) {
            files += "<a href=/" + fileEntry.getName() + ">" + fileEntry.getName() + "</a>" + "\n";
        }
        return files.getBytes();
    }
}
