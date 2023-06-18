package test.services.jsonParser;

import java.util.HashMap;

public class JsonBodyParser {
    private String jsonString;
    private HashMap<String, Object> jsonObject;

    public JsonBodyParser(String jsonString) {
        this.jsonString = jsonString;
        this.parseToMap();
    }

    public String getJsonString() {
        return this.jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
        this.parseToMap();
    }

    public HashMap<String, Object> getJsonObject() {
        return this.jsonObject;
    }

    private record JsonBlockString(String result, int startIndex, int endIndex) {}

    public void parseToMap() {
        if (this.jsonString == null || this.jsonString.strip().equals("")) return;

        String[] lines = this.jsonString.split("\n");
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

        this.jsonObject = result;
    }

    public HashMap<String, Object> parseToMap(String json) {
        if (json == null || json.strip().equals("")) return null;

        String[] lines = json.split("\n");
        HashMap<String, Object> result = new HashMap<>();
        String savedKey = null;
        HashMap<String, Object> savedKeyValue = new HashMap<>();

        for (int i = 0, length = lines.length; i < length; i++) {
            String lineStripped = lines[i].strip();

            if (lineStripped.charAt(0) == '{' || lineStripped.charAt(0) == '}') {
                result.put(savedKey, savedKeyValue);
                savedKey = null;
                continue;
            }

            if (savedKey != null) {
                if (lineStripped.charAt(lineStripped.length() - 1) == '{') {
                    JsonBlockString jsonBlock = getJsonBlockString(lines, i);
                    i = jsonBlock.endIndex;
                    savedKeyValue.putAll(parseToMap(jsonBlock.result));
                }
                else {
                    savedKeyValue.putAll(parseToMap(lineStripped));
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

    private JsonBlockString getJsonBlockString(String[] jsonLines, int startIndex) {
        String result = "";
        int endIndex = 0;
        int level = 0;

        for (int i = startIndex, length = jsonLines.length; i < length; i++) {
            result += jsonLines[i] + "\n";
            String lineStripped = jsonLines[i].replace("\"", "").strip();
            if (lineStripped.charAt(0) == '}') {
                if (level > 0) {
                    level--;
                    continue;
                }
                endIndex = i;
                break;
            }
            if (lineStripped.charAt(lineStripped.length() - 1) == '{' && i != startIndex) {
                level++;
            }
        }
        return new JsonBlockString(result, startIndex, endIndex);
    }

    public String getValue(String[] keys) {
        int index = 0;

        String key = keys[index];
        Object value = this.jsonObject.get(key);

        if (keys.length > 1 && value instanceof HashMap) {
            return getValue(keys, (HashMap<String, Object>) value, index + 1);
        } else {
            return value.toString();
        }
    }

    public String getValue(String[] keys, HashMap<String, Object> jsonParsed) {
        int index = 0;

        String key = keys[index];
        Object value = jsonParsed.get(key);

        if (keys.length > 1 && value instanceof HashMap) {
            return getValue(keys, (HashMap<String, Object>) value, index + 1);
        } else {
            return value.toString();
        }
    }

    private String getValue(String[] keys, HashMap<String, Object> jsonParsed, int index) {
        String key = keys[index];
        Object value = jsonParsed.get(key);

        if (keys.length - 1 > index && value instanceof HashMap) {
            return getValue(keys, (HashMap<String, Object>) value, index + 1);
        } else {
            return value.toString();
        }
    }
}

