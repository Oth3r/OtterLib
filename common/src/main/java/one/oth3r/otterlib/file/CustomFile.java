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
        if (!Files.exists(getFilePath())) log("Creating new `{}`", getFilePath().getFileName());
        File file = getFile();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(getFileSettings().getGson().toJson(this));
        }
        // if the file doesn't exist at this stage it would be because of a bad directory, try fileNotExist() to create the directory
        catch (NoSuchFileException ignored) {
            fileNotExist();
        }
        catch (Exception e) {
            error(String.format("There was an error saving '%s`: %s", file.getName(), e.getMessage()));
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
        if (!Files.exists(getFilePath())) fileNotExist();
        File file = getFile();

        // try reading the file
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            updateFromReader(reader);
        }
        // rare, but if the file doesn't exist at this stage, try again
        catch (NoSuchFileException ignored) {
            fileNotExist();
        }
        // cant load for some reason
        catch (Exception e) {
            error("ERROR LOADING '{}': {}", file.getName(),e.getMessage());
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
     * logic for the file not existing when loading, defaults to saving
     */
    default void fileNotExist() {
        // try to make the config directory
        try {
            Files.createDirectories(getFilePath().getParent().getFileName());
        } catch (Exception e) {
            error("Failed to create config directory. Canceling all config loading...");
            return;
        }
        save();
    }

    /**
     * logs info to the console if a logger is provided (via {@link FileSettings})
     */
    private void log(String string, Object args) {
        if (getFileSettings().isLogging()) {
            getFileSettings().getLogger().info(string, args);
        }
    }

    /**
     * logs an error to the console if a logger is provided (via {@link FileSettings})
     */
    private void error(String format, Object... args) {
        if (getFileSettings().isLogging()) {
            getFileSettings().getLogger().error(format,args);
        }
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    T clone();
}
