package one.oth3r.otterlib.file;

import com.google.gson.Gson;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.base.OtterLogger;


/**
 * settings to provide a file class to use when saving and loading
 */
public class FileSettings {
    protected OtterLogger logger;
    protected Gson gson;

    public FileSettings(OtterLogger logger, Gson gson) {
        this.logger = logger;
        this.gson = gson;
    }

    public FileSettings(OtterLogger logger) {
        this.logger = logger;
        this.gson = Assets.getGson();
    }

    public FileSettings() {
        this.gson = Assets.getGson();
    }

    public boolean isLogging() {
        return logger != null;
    }

    public OtterLogger getLogger() {
        return logger;
    }

    public void setLogger(OtterLogger logger) {
        this.logger = logger;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
