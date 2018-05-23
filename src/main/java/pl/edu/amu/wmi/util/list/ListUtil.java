package pl.edu.amu.wmi.util.list;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stworzone przez Eryk Mariankowski dnia 19.04.18.
 */
public class ListUtil {

    private ListUtil() {

    }

    public static <T, R> boolean equalByProperty(List<T> a, List<T> b, Function<T, R> mapper) {
        return a.stream().map(mapper).collect(Collectors.toList())
                .equals(b.stream().map(mapper).collect(Collectors.toList()));
    }

}
