package pl.edu.amu.wmi.util.pair;

import javafx.util.Pair;

/**
 * Stworzone przez Eryk Mariankowski dnia 09.05.18.
 */
public class PairUtil {

    private PairUtil() {

    }

    public static <T, S> Pair<T, S> of(T key, S value) {
        return new Pair<>(key, value);
    }

}
