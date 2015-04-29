package net.umatoma.comiguide.util;

public class FormUtil {
    public static String encodeNullToBlank(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }
}
