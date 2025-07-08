package one.oth3r.otterlib.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceReader {
    String pathToResource;
    // not null if the resource is in a jar file
    ClassLoader classLoader;

    /**
     * Creates a ResourceReader for reading resources from a normal file system
     * @param pathToResource the path to the resource, must end with a slash (e.g., "config/")
     */
    public ResourceReader(String pathToResource) {
        this.pathToResource = pathToResource;
        this.classLoader = null;
    }

    /**
     * Creates a ResourceReader for reading resources from a jar file
     * @param pathToResource the path to the resource, must end with a slash (e.g., "config/")
     * @param classLoader the classloader to use for reading the resource
     */
    public ResourceReader(String pathToResource, ClassLoader classLoader) {
        this.pathToResource = pathToResource;
        this.classLoader = classLoader;
    }

    /**
     * using the path to the resource, returns a BufferedReader for reading the resource
     * @param resource the name and extension of the resource (e.g., "config.json")
     * @return a BufferedReader for reading the resource
     */
    public BufferedReader getResource(String resource) throws IOException {
        String fullPath = pathToResource + resource;
        if (classLoader == null) {
            return Files.newBufferedReader(Paths.get(fullPath), StandardCharsets.UTF_8);
        }
        InputStream inputStream = classLoader.getResourceAsStream(fullPath);
        if (inputStream == null) throw new IOException("Resource not found: " + resource);
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @Override
    public String toString() {
        String name = pathToResource;
        if (classLoader != null) {
            return classLoader.getName()+": "+ name;
        }
        return name;
    }
}
