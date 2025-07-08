package one.oth3r.otterlib.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtterLogger implements SimpleLogger {
    Logger logger;

    public OtterLogger(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(String.format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(String.format(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(String.format(message, args));
    }
}
