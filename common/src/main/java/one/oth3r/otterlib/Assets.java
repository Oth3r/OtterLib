package one.oth3r.otterlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {
    public static final String ID = "otterlib";

    /**
     * gets a Gson with the LenientTypeAdapter
     */
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new LenientTypeAdapterFactory())
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
    }

    /**
     * the LenientTypeAdapter, doesn't throw anything when reading a weird JSON entry, good for human entered JSONs
     */
    @SuppressWarnings("unchecked")
    public static class LenientTypeAdapterFactory implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

            return new TypeAdapter<>() {
                // normal writer
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }
                // custom reader
                public T read(JsonReader in) throws IOException {
                    try {
                        //Try to read value using default TypeAdapter
                        return delegate.read(in);
                    } catch (Exception e) {
                        // skip the invalid json value
                        in.skipValue();

                        // try to provide a default instance for common types
                        Class<? super T> rawType = type.getRawType();

                        if (List.class.isAssignableFrom(rawType)) {
                            return (T) new ArrayList<>();
                        } else if (Map.class.isAssignableFrom(rawType)) {
                            return (T) new HashMap<>();
                        }

                        // attempt to create a new instance using a no-arg constructor
                        try {
                            Constructor<? super T> ctor = rawType.getDeclaredConstructor();
                            ctor.setAccessible(true);
                            return (T) ctor.newInstance();
                        } catch (Exception ex) {
                            // no default instance is available, fallback to null
                            return null;
                        }
                    }
                }
            };
        }
    }
}
