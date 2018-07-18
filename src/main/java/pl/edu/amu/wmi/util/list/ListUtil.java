package pl.edu.amu.wmi.util.list;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Stworzone przez Eryk Mariankowski dnia 19.04.18.
 */
public class ListUtil {

    private ListUtil() {

    }

    public static <T, S, U> Map<T, U> mapMapValues(Map<T, S> inputMap, Function<S, U> mapper) {
        return inputMap
            .entrySet()
            .stream()
            .collect(toMap(Map.Entry::getKey, e -> mapper.apply(e.getValue())));
    }

    public static <T, R> boolean equalByProperty(List<T> a, List<T> b, Function<T, R> mapper) {
        return a.stream().map(mapper).collect(Collectors.toList())
            .equals(b.stream().map(mapper).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] addToArray(T[] arr, T t) {
        List<T> arrayList = new ArrayList<>(Arrays.asList(arr));
        arrayList.add(t);
        T[] newArr = (T[]) Array.newInstance(t.getClass(), arr.length + 1);
        return arrayList.toArray(arrayList.toArray(newArr));
    }


}
