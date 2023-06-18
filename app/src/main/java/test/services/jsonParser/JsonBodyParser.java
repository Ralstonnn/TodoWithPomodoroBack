package test.services.jsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonBodyParser {
//    Figure out how to parse the json body into a HashMap.
//    TODO: Add comments
    private record JsonBlockString(String result, int startIndex, int endIndex) {}

    public static HashMap<String, Object> parseToMap(String json) {
        if (json == null || json.strip().equals("")) return null;

        String[] lines = json.split("\n");
        HashMap<String, Object> result = new HashMap<>();
        String savedKey = null;

        for (int i = 0, length = lines.length; i < length; i++) {
            String lineStripped = lines[i].strip();

            if (lineStripped.charAt(0) == '{' || lineStripped.charAt(0) == '}') {
                savedKey = null;
                continue;
            }

            if (savedKey != null) {
                if (lineStripped.charAt(lineStripped.length() - 1) == '{') {
                    JsonBlockString jsonBlock = getJsonBlockString(lines, i);
                    i = jsonBlock.endIndex;
                    result.put(savedKey, parseToMap(jsonBlock.result));
                }
                else {
                    result.put(savedKey, parseToMap(lineStripped));
                }
                continue;
            }

            String[] keyValue = lineStripped.split(":");
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").trim();

            if (value.equals("{")) {
                savedKey = key;
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    private static JsonBlockString getJsonBlockString(String[] jsonLines, int startIndex) {
        String result = "";
        int endIndex = 0;
        for (int i = startIndex, length = jsonLines.length; i < length; i++) {
            result += jsonLines[i] + "\n";
            if (jsonLines[i].replace("\"", "").strip().charAt(0) == '}') {
                endIndex = i;
                break;
            }
        }
        return new JsonBlockString(result, startIndex, endIndex);
    }
}

