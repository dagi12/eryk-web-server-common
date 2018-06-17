package pl.edu.amu.wmi.util;

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
        return !((s == null) || "".equals(s)) && s.trim().length() > 0;
    }

    public static String firstAndSecondName(String firstName, String secondName) {
        return firstName + " " + secondName;
    }

    public static String hostFromDataSourceUrl(String url) {
        return URI.create(url.substring(5).split(";")[0].split("\\\\")[0]).getHost();
    }

    public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
        if (enumName == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, enumName);
            return true;
        } catch (final IllegalArgumentException ex) {
            return false;
        }
    }


    public static String underscoreToCamelCase(String s) {
        String inputString = s.toLowerCase();
        StringBuilder builder = new StringBuilder();
        boolean doUppercase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (c != '_') {
                if (doUppercase) {
                    builder.append(Character.toUpperCase(c));
                    doUppercase = false;
                } else {
                    builder.append(c);
                }
            } else {
                doUppercase = true;
            }
        }
        return builder.toString();
    }


}
