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
}
