package one.oth3r.otterlib.chat;

/**
 * Creates concrete LoaderText implementations for APIs that need to preserve a caller's text subtype.
 *
 * @param <T> the concrete LoaderText implementation
 */
public interface LoaderTextFactory<T extends LoaderText<T>> {
    T empty();

    T literal(String text);

    default T copy(T text) {
        return text.clone();
    }

    default T fromObject(Object obj) {
        if (obj instanceof LoaderText<?>) return literal(obj.toString());
        return literal(String.valueOf(obj));
    }
}
