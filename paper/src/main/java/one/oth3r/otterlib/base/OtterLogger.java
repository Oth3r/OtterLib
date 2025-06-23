package one.oth3r.otterlib.base;

import java.util.logging.Logger;

public class OtterLogger implements SimpleLogger {
    private final Logger logger;

    public OtterLogger(String name) {
        logger = Logger.getLogger(name);
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(String.format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warning(String.format(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        logger.severe(String.format(message, args));
    }
}
