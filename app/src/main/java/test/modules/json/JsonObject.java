package test.modules.json;

import java.lang.reflect.Field;
import java.util.HashMap;

public class JsonObject {
    private HashMap<String, Object> jsonObject;

    private record JsonBlockString(String result, int startIndex, int endIndex) {}

    public JsonObject() {
        this.jsonObject = new HashMap<>();
    }
    public JsonObject(String jsonString) {
        this.setJsonObjectFromJsonString(jsonString);
    }
    public JsonObject(Object obj) {
        this.setJsonObjectFromObject(obj);
    }
    @Override
    public String toString() {
        return this.hashMapToJsonString(this.jsonObject);
    }

    public void addKeyValue(String key, Object value) {
        this.jsonObject.put(key, value);
    }

    public Object getValue(String[] keys) {
        Object value = null;
        for (String key: keys) {
            value = this.jsonObject.get(key);
        }
        return value;
    }

    private void setJsonObjectFromJsonString(String jsonString) {
        if (jsonString == null || jsonString.strip().equals("")) return;

        String[] lines = jsonString.split("\n");
        HashMap<String, Object> result = new HashMap<>();
        String savedKey = null;
        HashMap<String, Object> savedKeyValue = new HashMap<>();

        for (int i = 0, length = lines.length; i < length; i++) {
            String lineStripped = lines[i].strip();

            if (lineStripped.charAt(0) == '{' || lineStripped.charAt(0) == '}') {
                if (savedKey != null && savedKeyValue != null) {
                    result.put(savedKey, savedKeyValue);
                }
                savedKey = null;
                continue;
            }

            if (savedKey != null) {
                if (lineStripped.charAt(lineStripped.length() - 1) == '{') {
                    JsonBlockString jsonBlock = this.getJsonBlockString(lines, i);
                    i = jsonBlock.endIndex;
                    savedKeyValue.putAll(this.parseToMap(jsonBlock.result));
                }
                else {
                    savedKeyValue.putAll(this.parseToMap(lineStripped));
                }
                continue;
            }

            String[] keyValue = lineStripped.split(":");
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").replace(",", "").trim();

            if (value.equals("{")) {
                savedKey = key;
            } else {
                if (isStringInstanceOfBoolean(value)) {
                    result.put(key, "true".equals(value));
                } else if (isStringInstanceOfNumber(value)) {
                    result.put(key, this.parseJsonStringToNumber(value));
                } else {
                    result.put(key, value);
                }
            }
        }

        this.jsonObject = result;
    }

    private void setJsonObjectFromObject(Object obj) {
        HashMap<String, Object> result = new HashMap<>();
        Field[] fields = obj.getClass().getFields();

        for (int i = 0, length = fields.length; i < length; i++) {
            String fieldName = fields[i].getName();
            Object fieldValue = null;
            try {
                fieldValue = fields[i].get(obj);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            result.put(fieldName, fieldValue);
        }
        this.jsonObject = result;
    }

    private String hashMapToJsonString(HashMap<String, Object> map) {
        StringBuilder sb = new StringBuilder("{\n");
        String[] mapKeys = map.keySet().toArray(new String[0]);

        for (int i = 0, length = mapKeys.length; i < length; i++) {
            sb.append("\"").append(mapKeys[i]).append("\"").append(": ");
            String value = "";
            Object currentMapValue = map.get(mapKeys[i]);
            if (currentMapValue instanceof HashMap) {
                value = this.hashMapToJsonString((HashMap<String, Object>) map.get(mapKeys[i]));
            } else {
                if (currentMapValue instanceof String) {
                    value = "\"" + currentMapValue + "\"";
                } else {
                    value = currentMapValue.toString();
                }
            }
            sb.append(value).append(",");
        }

        sb.append("\n}");
//        sb.replace("\n\n", "\n");
        return sb.toString();
    }

    private HashMap<String, Object> parseToMap(String json) {
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
                    JsonBlockString jsonBlock = this.getJsonBlockString(lines, i);
                    i = jsonBlock.endIndex;
                    savedKeyValue.putAll(this.parseToMap(jsonBlock.result));
                }
                else {
                    savedKeyValue.putAll(this.parseToMap(lineStripped));
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

    private Number parseJsonStringToNumber(String str) {
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
