package net.venedi.util;

import java.util.Collection;
import java.util.Map;

public final class ObjectUtil {

    private ObjectUtil() {}

    /**
     * Safe equality check
     * @param one
     * @param two
     * @return true if the objects are equal
     */
    public static boolean equals(Object one, Object two) {
        if (one == null && two != null)
            return false;
        if (one != null && !one.equals(two))
            return false;

        return true;
    }

    /**
     * @param str
     * @return true if the string is empty or null
     */
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    /**
     *
     * @param collection
     * @return true if collection is null or empty
     */
    public static boolean isEmptyOrNull(Collection<?> collection){
        return collection==null || collection.isEmpty();
    }

    /**
     * @param strings
     * @return true if all strings are empty or null
     */
    public static boolean isEmptyOrNull(String ... strings) {
        for (String s : strings) {
            if (!isEmptyOrNull(s))
                return false;
        }

        return true;
    }

    /**
     * @param <T> any type
     * @param value
     * @param defaultValue
     * @return value if value is not null otherwise defaultValue
     */
    public static <T> T ifNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Answers value of defaultValue if value is empty. For String or Collection<?> checks isEmpty() method for other objects acts as ifNull.
     *
     * @param <T> any type
     * @param value
     * @param defaultValue
     * @return value if value is null false or for String and Collection<?> isEmpty is false returns default value
     */
    public static <T> T ifEmpty(T value, T defaultValue) {
        if (isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    /**
     * @param value
     * @return true if value is null and if String or Collection<?> isEmpty is true
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String) {
            return ((String) value).isEmpty();
        }

        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).isEmpty();
        }

        if (value instanceof Map<?, ?>) {
            return ((Map<?, ?>) value).isEmpty();
        }

        return false;
    }

}
