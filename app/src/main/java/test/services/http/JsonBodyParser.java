package test.services.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonBodyParser {
//    Figure out how to parse the json body into a HashMap.
//    TODO: Add comments
    public static HashMap<String, Object> parseToMap(String json) {
        if (json == null || json.strip().equals("")) return null;

        String[] lines = json.split("\n");
        HashMap<String, Object> result = new HashMap<>();
        String savedKey = null;

        for (String line : lines) {
            String lineStripped = line.strip();

            if (lineStripped.charAt(0) == '{' || lineStripped.charAt(0) == '}') {
                savedKey = null;
                continue;
            }

            if (savedKey != null) {
                result.put(savedKey, parseToMap(lineStripped));
                continue;
            }

            String[] keyValue = lineStripped.split(":");
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1];

            if (value.strip().equals("{")) {
                savedKey = key;
            } else {
                result.put(key, value.replace("\"", "").trim());
            }
        }

        return result;
    }
}
