package one.oth3r.otterlib.base;

public interface SimpleLogger {
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Object... args);
}
