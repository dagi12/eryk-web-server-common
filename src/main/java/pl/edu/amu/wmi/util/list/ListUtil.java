package pl.edu.amu.wmi.util.list;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Stworzone przez Eryk Mariankowski dnia 19.04.18.
 */
public final class ListUtil {

    private ListUtil() {

    }

    public static <T, S, U> Map<T, U> mapMapValues(Map<T, S> inputMap, Function<S, U> mapper) {
        if (inputMap != null) {
            return inputMap
                    .entrySet()
                    .stream()
                    .collect(toMap(Map.Entry::getKey, e -> mapper.apply(e.getValue())));
        }
        return null;
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

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list, Class<T> tClass) {
        if (isEmpty(list)) {
            return (T[]) Array.newInstance(tClass, 0);
        }
        T[] instance = (T[]) Array.newInstance(tClass, list.size());
        return list.toArray(instance);
    }

    public static <T> boolean findDuplicates(List<T> listContainingDuplicates) {
        final Set<T> setToReturn = new HashSet<>();
        final Set<T> set1 = new HashSet<>();

        for (T yourInt : listContainingDuplicates) {
            if (!set1.add(yourInt)) {
                return true;
            }
        }
        return false;
    }

}
