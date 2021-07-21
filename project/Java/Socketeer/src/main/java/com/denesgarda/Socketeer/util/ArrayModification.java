package com.denesgarda.Socketeer.util;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ArrayModification {
    public static<T> T[] append(T[] array, T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }
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
