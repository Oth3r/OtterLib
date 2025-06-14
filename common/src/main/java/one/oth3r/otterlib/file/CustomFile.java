package one.oth3r.otterlib.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public interface CustomFile<T extends CustomFile<T>> {
    FileSettings getFileSettings();
    /**
     * the path to the file - including the extension ex. usr/config/custom-file.json
     */
    Path getFilePath();

    void reset();

    /**
     * saves the current instance to file
     */
    default void save() {
        if (!Files.exists(getFilePath())) {
            log("Creating new `{}`", getFilePath().getFileName());
            createDirectory();
        }
        File file = getFile();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(getFileSettings().getGson().toJson(this));
        }
        catch (Exception e) {
            error(String.format("There was an error saving '%s`: %s", file.getName(), e));
        }
    }

    /**
     * overload of {@link #load(boolean)} but with save after loading turned on by default
     */
    default void load() {
        load(true);
    }

    /**
     * loads the file to the current instance using updateFromReader()
     * @param save weather or not to save after loading, will get rid of bad data that wasn't able to load
     */
    default void load(boolean save) {
        if (!Files.exists(getFilePath())) save();
        File file = getFile();

        // try reading the file
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            updateFromReader(reader);
        }
        // rare, if the file doesn't exist at this stage, save (it should have gotten caught in the first if statement, but sometimes it doesn't)
        catch (NoSuchFileException ignored) {
            save();
        }
        // cant load for some reason
        catch (Exception e) {
            error("ERROR LOADING '{}': {}", file.getName(),e);
        }
        // save after loading (if enabled)
        if (save) save();
    }

    default void updateFromReader(BufferedReader reader) {
        // try to read the json
        T file;
        JsonElement json = JsonParser.parseReader(reader);
        try {
            file = getFileSettings().getGson().fromJson(json, getFileClass());
        } catch (Exception e) {
            throw new NullPointerException();
        }

        // if the file couldn't be parsed, (null) try using the custom update method using the JsonElement on the current file
        // if not use the new file object that is loaded with the file data, and call update using that
        if (file == null) {
            this.update(json);
        } else {
            // update the instance
            file.update(json);
            // load the file to the current object
            copyFileData(file);
        }
    }

    default File getFile() {
        return getFilePath().toFile();
    }

    /**
     * @return the class of the File
     */
    Class<T> getFileClass();

    /**
     * loads the data from the file object into the current object - DEEP COPY
     * @param newFile the file to take the properties from
     */
    void copyFileData(T newFile);

    /**
     * updates the file based on the version number of the current instance
     */
    void update(JsonElement json);

    /**
     * deprecated in favor of {@link #createDirectory()}
     */
    @Deprecated(since = "0.1.1", forRemoval = true)
    default void fileNotExist() {
        createDirectory();
    }

    /**
     * creates the directory that the file is located in
     */
    default void createDirectory() {
        try {
            Files.createDirectories(getFilePath().getParent());
        } catch (Exception e) {
            error("Failed to create config directory... {}", e);
        }
    }

    /**
     * logs info to the console if a logger is provided (via {@link FileSettings})
     */
    private void log(String string, Object args) {
        if (getFileSettings().isLogging()) {
            getFileSettings().getLogger().info(String.format(string, args));
        }
    }

    /**
     * logs an error to the console if a logger is provided (via {@link FileSettings})
     */
    private void error(String string, Object... args) {
        if (getFileSettings().isLogging()) {
            getFileSettings().getLogger().severe(String.format(string,args));
        }
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    T clone();
}
