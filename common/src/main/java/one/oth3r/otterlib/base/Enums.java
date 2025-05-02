package one.oth3r.otterlib.base;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Enums {
    /**
     * converts an arraylist of enums to an arraylist of strings of the enum names using {@link Enum#name()}
     * @param enumList the arraylist of Enums
     * @return the converted arraylist of strings
     */
    public static <T extends Enum<T>> ArrayList<String> toStringList(ArrayList<T> enumList) {
        return (ArrayList<String>) enumList.stream().map(Enum::name).collect(Collectors.toList());
    }

    /**
     * method overload of {@link #toStringList(ArrayList)}
     * @param enumArray an array of custom enum
     */
    public static <T extends Enum<T>> ArrayList<String> toStringList(T[] enumArray) {
        return toStringList(new ArrayList<>(List.of(enumArray)));
    }

    /**
     * turns an {@link ArrayList} of enum strings into an ArrayList of {@link  Enum} <br>
     * if the enum cannot be found for one of the strings in the list, it will be skipped
     * @param stringList the list of enum strings
     * @param enumType the class of the enum
     * @return the converted {@link ArrayList} of enums
     */
    public static <T extends Enum<T>> ArrayList<T> toEnumList(ArrayList<String> stringList, Class<T> enumType) {
        return toEnumList(Enum::valueOf, stringList, enumType);
    }
    public static <T extends Enum<T>> ArrayList<T> toEnumList(BiFunction<Class<T>,String, T> function, ArrayList<String> stringList, Class<T> enumType) {
        ArrayList<T> moduleList = new ArrayList<>();

        // for each string in the enum string list
        for (String module : stringList) {
            // try to get the Enum from the string, if it throws an Exception, ignore it (invalid strings get ignored)
            try {
                T enumValue = function.apply(enumType, module);
                moduleList.add(enumValue);
            } catch (IllegalArgumentException ignored) {}
        }

        // return the new list
        return moduleList;
    }

    /**
     * finds the next enum in the enum array, wrapping around if the enum is at the end of the array
     * @param current the current enum
     * @param enumType the class type of the enum
     * @param exclude the list of enums to 'skip' over when finding the next enum
     * @return the next enum in the array
     */
    @SafeVarargs
    public static <T extends Enum<T>> T next(T current, Class<T> enumType, T... exclude) {
        T[] values = enumType.getEnumConstants();
        // get the next enum in the values list, wrapping around if necessary
        T next = values[(current.ordinal()+1)%values.length];

        // if the exclude list isn't empty
        if (exclude != null) {
            // if the next enum is in the exclude list, find the next one after the current next one
            if (Arrays.asList(exclude).contains(next)) return next(next, enumType, exclude);
        }

        // return the next Enum
        return next;
    }

    /**
     * gets an enum from a string without returning null
     * @param enumString the string of the enum
     * @param enumType the class of enums
     * @return an enum, if there isn't a match, it returns the first enum
     */
    public static <T extends Enum<T>> T get(Object enumString, Class<T> enumType) {
        T[] values = enumType.getEnumConstants();
        // if there's no match return the first entry
        return search(enumString,enumType).orElse(values[0]);
    }

    /**
     * searches for an enum constant by checking against its toString() method. Overload of {@link #search(Predicate, Class)} <br>
     * if multiple matches are found, the first is returned <br>
     * string case is ignored
     *
     * @param <T> the type of the enum
     * @return an {@code Optional} containing the matching enum constant if found, otherwise an empty {@code Optional}
     */
    public static <T extends Enum<T>> Optional<T> search(Object enumString, Class<T> enumType) {
        return search((s) -> s.toString().equalsIgnoreCase((String) enumString), enumType).stream().findFirst();
    }

    /**
     * searches for enum constants by filtering by the provided predicate
     * @param predicate the filter to use to collect the enums
     * @param enumType the class of the enum that is being searched
     * @return a list of enums
     * @param <T> the type of enum
     */
    public static <T extends Enum<T>> List<T> search(Predicate<T> predicate, Class<T> enumType) {
        return EnumSet.allOf(enumType).stream().filter(predicate).toList();
    }

    /**
     * checks if an Enum string is valid in the enum type, Overload of {@link #search(Object, Class)}
     */
    public static <T extends Enum<T>> boolean contains(Object enumString, Class<T> enumType) {
        return search(enumString,enumType).isPresent();
    }
}
