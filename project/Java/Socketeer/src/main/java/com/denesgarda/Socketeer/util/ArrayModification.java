package com.denesgarda.Socketeer.util;

import java.util.Arrays;

/**
 * Utilities used within the server to modify arrays
 * @author denesgarda
 */
public class ArrayModification {

    /**
     * Append an element to the end of an array
     * @param array The array the element should be added to
     * @param value The element to be added
     * @param <T> The type of array and element
     * @return The new array
     */
    public static<T> T[] append(T[] array, T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

    /**
     * Remove an element from an array
     * @param array The array the element should be removed from
     * @param value The element to be removed
     * @param <T> The type of array and element
     * @return The new array
     */
    public static<T> T[] remove(T[] array, T value) {
        if (array == null) {
            return array;
        }
        T[] anotherArray = Arrays.copyOf(array, array.length - 1);
        for (int i = 0, k = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                continue;
            }
            anotherArray[k++] = array[i];
        }
        return anotherArray;
    }
}
