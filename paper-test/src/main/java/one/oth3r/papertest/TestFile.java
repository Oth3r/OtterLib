package one.oth3r.papertest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import one.oth3r.otterlib.file.CustomFile;
import one.oth3r.otterlib.file.FileSettings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

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
        return new FileSettings(Logger.getLogger("fabric-test"));
    }

    @Override
    public Path getFilePath() {
        return Paths.get(PaperTest.configDir,"test.json");
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
    public void update(JsonElement jsonElement) {
        JsonObject file = jsonElement.getAsJsonObject();
        // can do both ways
//        if (file.get("version").getAsDouble() == 1.0) {
        if (this.version == 1.0) {
            this.version = 1.1;
            this.test = !file.get("test-boolean").getAsBoolean();
        }
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
