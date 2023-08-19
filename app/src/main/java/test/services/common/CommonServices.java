package test.services.common;

import java.io.InputStream;

public class CommonServices {
    public static void closeInputStream(InputStream is) {
        try {
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Number parseStringToNumber(String str) {
        if (str.contains(".")) {
            return Double.parseDouble(str);
        } else {
            return Integer.parseInt(str);
        }
    }

    public static boolean isStringInstanceOfBoolean(String str) {
        return "true".equals(str) || "false".equals(str);
    }
    public static boolean isStringInstanceOfNumber(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
