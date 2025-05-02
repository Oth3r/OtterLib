package one.oth3r.otterlib.file;

import com.google.gson.Gson;
import one.oth3r.otterlib.Assets;
import org.slf4j.Logger;


/**
 * settings to provide a file class to use when saving and loading
 */
public class FileSettings {
    protected Logger logger;
    protected Gson gson;

    public FileSettings(Logger logger, Gson gson) {
        this.logger = logger;
        this.gson = gson;
    }

    public FileSettings(Logger logger) {
        this.logger = logger;
        this.gson = Assets.getGson();
    }

    public FileSettings() {
        this.gson = Assets.getGson();
    }

    public boolean isLogging() {
        return logger != null;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
