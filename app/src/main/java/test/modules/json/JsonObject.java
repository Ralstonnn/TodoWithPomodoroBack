package test.modules.json;

import com.google.common.primitives.Chars;
import test.services.common.CommonServices;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonObject {
    private HashMap<String, Object> jsonObject = new HashMap<>();

    public JsonObject() {
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
            if (value instanceof JsonObject) {
                value = ((JsonObject) value).getValue(new String[]{keys[i]});
            }
        }
        return value;
    }

    private void setJsonObjectFromJsonString(String str) {
        char[] strArr = str.trim().toCharArray();
        int strArrStart = -1,
                strArrEnd = -1;

        for (int i = 0, length = strArr.length; i < length; i++) {
            if (strArr[i] == '{') {
                strArrStart = i + 1;
                break;
            }
        }
        for (int i = strArr.length - 1; i >= strArrStart; i--) {
            if (strArr[i] == '}') {
                strArrEnd = i;
                break;
            }
        }

        if (strArrEnd == -1 || strArrEnd == 1) return;

        enum itemType {arr, obj, str}
        char[] charactersToSkip = new char[]{' ', '\n', '\t'};
        itemType currentItemType = null;
        ArrayList<Character> charsToMatch = new ArrayList<>();
        StringBuilder currentKey = new StringBuilder();
        boolean isKeySet = false;
        StringBuilder currentValue = new StringBuilder();

        for (int i = strArrStart; i < strArrEnd; i++) {
            if (Chars.contains(charactersToSkip, strArr[i]) && currentItemType == null && i < strArrEnd - 1) {
                continue;
            }
            if (!isKeySet) {
                if (strArr[i] == ':' && currentKey.length() > 0) isKeySet = true;
                else if ((strArr[i] != '"' || (i > strArrStart && strArr[i - 1] == '\\')) && strArr[i] != ',')
                    currentKey.append(strArr[i]);
                continue;
            } else {
                currentValue.append(strArr[i]);
            }

            int charsToMatchLastIndex = charsToMatch.size() - 1;

            switch (strArr[i]) {
                case '{':
                case '[':
                    if (currentItemType == null)
                        currentItemType = strArr[i] == '{' ? itemType.obj : itemType.arr;
                    else if (charsToMatch.size() == 0)
                        charsToMatch.add(strArr[i]);
                    break;
                case '}':
                case ']':
                    if ((currentItemType == itemType.obj || currentItemType == itemType.arr) && charsToMatch.size() == 0) {
                        if (strArr[i] == '}')
                            this.jsonObject.put(currentKey.toString(), new JsonObject(currentValue.toString()));
                        else
                            this.jsonObject.put(currentKey.toString(), new JsonArray(currentValue.toString()));
                        currentKey.delete(0, currentKey.length());
                        currentValue.delete(0, currentValue.length());
                        isKeySet = false;
                        currentItemType = null;
                    } else if (charsToMatchLastIndex > -1) {
                        if ((strArr[i] == '}' && charsToMatch.get(charsToMatchLastIndex) == '{') ||
                                (strArr[i] == ']' && charsToMatch.get(charsToMatchLastIndex) == '[')) {
                            charsToMatch.remove(charsToMatchLastIndex);
                        }
                    }
                    break;
                case '"':
                    if (currentItemType == null) currentItemType = itemType.str;
                    else if (strArr[i - 1] != '\\') {
                        if (currentItemType == itemType.str) {
                            this.jsonObject.put(currentKey.toString(), currentValue.substring(1, currentValue.length() - 1));
                            currentKey.delete(0, currentKey.length());
                            currentValue.delete(0, currentValue.length());
                            isKeySet = false;
                            currentItemType = null;
                        } else if (charsToMatchLastIndex > -1 && charsToMatch.get(charsToMatchLastIndex) == '"') {
                            charsToMatch.remove(charsToMatchLastIndex);
                        } else {
                            charsToMatch.add('"');
                        }
                    }
                    break;
                case ',':
                    if (currentKey.length() <= 1) {
                        currentKey.delete(0, currentKey.length());
                        break;
                    }
                    if (currentItemType == null) {
                        String value = currentValue.substring(0, currentValue.length() - 1);
                        if (CommonServices.isStringInstanceOfBoolean(value)) {
                            this.jsonObject.put(currentKey.toString(), "true".equals(value));
                        } else if (CommonServices.isStringInstanceOfNumber(value)) {
                            this.jsonObject.put(currentKey.toString(), CommonServices.parseStringToNumber(value));
                        }
                        currentKey.delete(0, currentKey.length());
                        currentValue.delete(0, currentValue.length());
                        isKeySet = false;
                    }
                    break;
            }
            if (currentValue.length() > 0 && i == strArrEnd - 1 && currentItemType == null) {
                String value = currentValue.toString().trim();
                if (CommonServices.isStringInstanceOfBoolean(value)) {
                    this.jsonObject.put(currentKey.toString(), "true".equals(value));
                } else if (CommonServices.isStringInstanceOfNumber(value)) {
                    this.jsonObject.put(currentKey.toString(), CommonServices.parseStringToNumber(value));
                }
                currentKey.delete(0, currentKey.length());
                currentValue.delete(0, currentValue.length());
                isKeySet = false;
            }
        }
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
                    value = "\"" + ((String) currentMapValue).replace("\"", "\\\"") + "\"";
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
}
