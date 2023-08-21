package test.modules.json;

import test.services.common.CommonServices;

import java.lang.reflect.Field;
import java.util.HashMap;

public class JsonObject {
    private HashMap<String, Object> jsonObject;

    private record JsonBlockString(String result, int startIndex, int endIndex) {
    }

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
        Object value = this.jsonObject.get(keys[0]);
        for (int i = 1; i < keys.length; i++) {
            if (value instanceof HashMap) {
                value = ((HashMap) value).get(keys[i]);
            }
        }
        return value;
    }

    private String[] getKeyValueFromJsonString(String str) {
        String[] result = new String[2];
        int indexOfColon = str.indexOf(':');
        result[0] = str.substring(0, indexOfColon);
        result[1] = str.substring(indexOfColon + 1);
        return result;
    }

    private String jsonStringAddEntersToObjectBrackets(String str) {
        str = str.strip();
        if (str.length() < 2)
            return str;
        if (str.charAt(0) == '{' && str.charAt(1) != '\n') {
            str = "{\n" + str.substring(1);
        }
        if (str.charAt(str.length() - 1) == '}' && str.charAt(str.length() - 2) != '\n') {
            str = str.substring(0, str.length() - 1) + "\n}";
        }
        String[] strSplit = str.split("\n");
        for (int i = 0, length = strSplit.length; i < length; i++) {
            int indexOfColon = strSplit[i].indexOf(':');
            if (indexOfColon > -1) {
                String key = strSplit[i].substring(0, indexOfColon);
                String value = strSplit[i].substring(indexOfColon + 1);
                strSplit[i] = key + ":" + this.jsonStringAddEntersToObjectBrackets(value);
            }
        }
        return String.join("\n", strSplit);
    }

    private String[] jsonStringSplit(String str) {
        str = this.jsonStringAddEntersToObjectBrackets(str);

        String[] lines = str.split("\n");
        int initialLength = lines.length;
        boolean wasChanged = false;

        for (int i = 0; i < lines.length; i++) {
            // Checking for embedded json elements
            String[] tmp = lines[i].split(",");
            // If string has embedded elements the adding enters to each one
            if (tmp.length > 1) {
                for (int j = 0; j < tmp.length; j++) {
                    tmp[j] = this.jsonStringAddEntersToObjectBrackets(tmp[j]);
                }
                lines[i] = String.join(",\n", tmp);
                wasChanged = true;
            }
        }
        // If lines were changed then running the function again and returning result
        if (wasChanged) {
            String linesJoined = String.join("\n", lines);
            lines = linesJoined.split("\n");
            if (lines.length != initialLength) {
                return this.jsonStringSplit(linesJoined);
            }
        }

        return lines;
    }

    private void setJsonObjectFromJsonString(String jsonString) {
        if (jsonString == null || jsonString.strip().equals(""))
            return;

        String[] lines = this.jsonStringSplit(jsonString);
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
                } else {
                    savedKeyValue.putAll(this.parseToMap(lineStripped));
                }
                continue;
            }

            String[] keyValue = this.getKeyValueFromJsonString(lineStripped);
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").replace(",", "").trim();

            if (value.equals("{")) {
                savedKey = key;
            } else {
                if (CommonServices.isStringInstanceOfBoolean(value)) {
                    result.put(key, "true".equals(value));
                } else if (CommonServices.isStringInstanceOfNumber(value)) {
                    result.put(key, CommonServices.parseStringToNumber(value));
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
            sb.append(value);
            if (i < length - 1) {
                sb.append(",");
            }
        }

        sb.append("\n}");
        // sb.replace("\n\n", "\n");
        return sb.toString();
    }

    private HashMap<String, Object> parseToMap(String json) {
        if (json == null || json.strip().equals(""))
            return null;

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
                } else {
                    savedKeyValue.putAll(this.parseToMap(lineStripped));
                }
                continue;
            }

            String[] keyValue = this.getKeyValueFromJsonString(lineStripped);
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").replace(",", "").trim();

            if (value.equals("{")) {
                savedKey = key;
            } else {
                if (CommonServices.isStringInstanceOfBoolean(value)) {
                    result.put(key, "true".equals(value));
                } else if (CommonServices.isStringInstanceOfNumber(value)) {
                    result.put(key, CommonServices.parseStringToNumber(value));
                } else {
                    result.put(key, value);
                }
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
}
