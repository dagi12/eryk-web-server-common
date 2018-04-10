package pl.edu.amu.wmi.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URI;

/**
 * Stworzone przez Eryk Mariankowski dnia 21.07.2017.
 */
public class MyStringUtil {

    private MyStringUtil() {
    }

    public static String getLastCharacters(String s, int i) {
        if (s == null || s.length() < i) {
            return null;
        } else if (s.length() == i) {
            return s;
        }
        return s.substring(s.length() - i);
    }

    public static String removeWhitespace(String s) {
        if (s != null) {
            return s.replaceAll("\\s+", "");
        }
        return null;
    }

    public static String toUpperCase(String s) {
        if (s != null) {
            return s.toUpperCase();
        }
        return null;
    }

    public static String extension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public static boolean isNotWhitespace(String s) {
        return !StringUtils.isEmpty(s) && s.trim().length() > 0;
    }

    public static String firstAndSecondName(String firstName, String secondName) {
        return firstName + " " + secondName;
    }

    public static String hostFromDataSourceUrl(String url) {
        return URI.create(url.substring(5).split(";")[0].split("\\\\")[0]).getHost();
    }

}
