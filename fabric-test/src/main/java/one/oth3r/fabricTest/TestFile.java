package one.oth3r.fabricTest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.file.CustomFile;
import one.oth3r.otterlib.file.FileSettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestFile implements CustomFile<TestFile> {

    @SerializedName("version")
    private double version = 1.1;
    @SerializedName("test-boolean")
    private Boolean test = true;
    @SerializedName("string")
    private String string = "";

    public TestFile() {}

    @Override
    public FileSettings getFileSettings() {
        return new FileSettings(new OtterLogger("test-file"));
    }

    @Override
    public Path getFilePath() {
        return Paths.get(FabricLoader.getInstance().getConfigDir().toString(),"test.json");
    }

    @Override
    public void reset() {
        copyFileData(new TestFile());
    }

    @Override
    public Class<TestFile> getFileClass() {
        return TestFile.class;
    }

    @Override
    public void copyFileData(TestFile testFile) {
        this.version = testFile.version;
        this.test = testFile.test;
        this.string = testFile.string;
    }

    @Override
    public JsonElement updateFromJSON(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        double version = obj.get("version").getAsDouble();
        if (version == 1.0) {
            version = 1.1;
            // this update removes the old-boolean property in place of the new test-boolean property
            // (which when updating is the inverse of old-boolean)
            obj.addProperty("test-boolean", !obj.get("old-boolean").getAsBoolean());
            obj.remove("old-boolean");
        }
        // set the fixed version
        obj.addProperty("version", version);


        return CustomFile.super.updateFromJSON(json);
    }

    @Override
    public void updateInstance() {
        // this method is called after the JSON has been loaded
        // we will make sure that the string is all caps after initialization
        string = string.toUpperCase();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TestFile testFile = (TestFile) o;
        return Objects.equals(test, testFile.test) && Objects.equals(string, testFile.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test, string);
    }

    @Override
    public TestFile clone() {
        TestFile clone = new TestFile();
        clone.copyFileData(this);
        return clone;
    }
}
