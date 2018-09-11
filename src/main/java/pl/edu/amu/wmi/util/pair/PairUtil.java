package pl.edu.amu.wmi.util.pair;

/**
 * Stworzone przez Eryk Mariankowski dnia 09.05.18.
 */
public final class PairUtil {

    private PairUtil() {

    }

    public static <T, S> Pair<T, S> of(T key, S value) {
        return new Pair<>(key, value);
    }

}
