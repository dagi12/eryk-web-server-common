package pl.edu.amu.wmi.util.list;

import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Stworzone przez Eryk Mariankowski dnia 19.04.18.
 */
public class ListUtil {

    private ListUtil() {

    }

    public static <T> boolean equalByProperty(List<T> a, List<T> b, ToIntFunction<? super T> mapper) {
        return a.stream().mapToInt(mapper).equals(b.stream().mapToInt(mapper));
    }

}
