package test.services.encryption;

import test.modules.json.JsonObject;
import test.services.Env;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;


public class EncryptionService {
    private static final String SECRET_KEY = Env.get("SECRET");
    private static final String ALGORITHM = "HmacSHA256";
    private static final String HEADER_STRING = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}";

    public static String generateJwt(JsonObject jo) throws NoSuchAlgorithmException, InvalidKeyException {
        Date currentDate = new Date();
        jo.addKeyValue("iat", currentDate.getTime());
        jo.addKeyValue("jti", generateJwtId());
        String headerEncoded = encodeBase64(HEADER_STRING);
        String dataEncoded = encodeBase64(jo.toString());
        String checksum = encryptHmacSha(String.format("%s.%s", headerEncoded, dataEncoded));
        return String.format("%s.%s.%s", headerEncoded, dataEncoded, checksum);
    }

    public static boolean verifyJwt(String jwt) throws NoSuchAlgorithmException, InvalidKeyException {
        String[] jwtSplit = jwt.split("\\.");
        if (jwtSplit.length != 3) return false;
        String header = jwtSplit[0];
        String body = jwtSplit[1];
        String checksum = jwtSplit[2];
        String controlChecksum = encryptHmacSha(String.format("%s.%s", header, body));
        return checksum.equals(controlChecksum);
    }

    private static String generateJwtId() {
        Random rand = new Random();
        final int upperBound = 10;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            result.append(rand.nextInt(upperBound));
        }
        return result.toString();
    }

    public static String encodeBase64(String str) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(str.getBytes());
    }

    public static String decodeBase64(String str) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(str);
        return new String(decodedBytes);
    }

    public static String encryptHmacSha(String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secretKeySpec);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes()));
    }
}
