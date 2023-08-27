package test.services.encryption;

import test.services.Env;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class EncryptionService {
    private static final String SECRET_KEY = Env.get("SECRET");
    private static final String ALGORITHM = "HmacSHA256";
    private static final String HEADER_STRING = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}";

    public static String generateJWT(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        String headerEncoded = encodeBase64(HEADER_STRING);
        String dataEncoded = encodeBase64(data);
        String sha = generateChecksum(String.format("%s.%s", headerEncoded, dataEncoded));
        return String.format("%s.%s.%s", headerEncoded, dataEncoded, sha);
    }

    private static String encodeBase64(String str) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(str.getBytes());
    }

    public static String decodeBase64(String str) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(str);
        return new String(decodedBytes);
    }

    private static String generateChecksum(String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secretKeySpec);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
    }

    public static String encryptWithKey(String data) {
        byte[] keyBytes = SECRET_KEY.getBytes();
        byte[] dataBytes = data.getBytes();
        byte[] result = new byte[dataBytes.length];
        int keyLength = keyBytes.length;
        int currentKeyIndex = 0;

        for (int i = 0, length = dataBytes.length; i < length; i++) {
            result[i] = (byte) (dataBytes[i] + keyBytes[currentKeyIndex]);
            if (currentKeyIndex < keyLength) {
                currentKeyIndex += 1;
            } else {
                currentKeyIndex = 0;
            }
        }

        return new String(result);
    }
}
