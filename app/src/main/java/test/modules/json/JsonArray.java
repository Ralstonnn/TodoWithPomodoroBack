package test.modules.json;

import com.google.common.primitives.Chars;
import test.services.common.CommonServices;

import java.sql.Array;
import java.util.ArrayList;

public class JsonArray {
    private ArrayList<Object> jsonArray = new ArrayList<>();

    public JsonArray(Object[] objects) {
        for (Object obj : objects) {
            if (obj instanceof Boolean || obj instanceof Number || obj instanceof String) {
                this.jsonArray.add(obj);
            } else if (obj instanceof Array) {
                this.jsonArray.add(new JsonArray((Object[]) obj));
            } else {
                this.jsonArray.add(new JsonObject(obj));
            }
        }
    }

    public JsonArray(String str) {
        if (str.length() <= 0) {
            return;
        }
        this.jsonArrayFromString(str);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0, lenght = this.jsonArray.size(); i < lenght; i++) {
            if (this.jsonArray.get(i) instanceof String) {
                result.append("\"" + this.jsonArray.get(i) + "\"");
            } else {
                result.append(this.jsonArray.get(i).toString());
            }

            if (i < lenght - 1)
                result.append(",");
        }
        result.append("]");
        return result.toString();
    }

    private void jsonArrayFromString(String str) {
        char[] strArr = str.trim().toCharArray();
        int strArrStart = -1,
                strArrEnd = -1;

        // Get json array string start and end position
        for (int i = 0, length = strArr.length; i < length; i++) {
            if (strArr[i] == '[') {
                strArrStart = i + 1;
                break;
            }
        }
        for (int i = strArr.length - 1; i >= strArrStart; i--) {
            if (strArr[i] == ']') {
                strArrEnd = i;
                break;
            }
        }

        if (strArrStart == -1 || strArrEnd == -1) return;

        enum itemType {arr, obj, str}
        char[] charactersToSkip = new char[]{' ', '\n', '\t'};
        itemType currentItemType = null;
        ArrayList<Character> charsToMatch = new ArrayList<>();
        StringBuilder currentItem = new StringBuilder();
        for (int i = strArrStart; i < strArrEnd; i++) {
            if (Chars.contains(charactersToSkip, strArr[i]) && currentItemType == null && i < strArrEnd - 1) {
                continue;
            }
            currentItem.append(strArr[i]);
            int charsToMatchLastIndex = charsToMatch.size() - 1;
            switch (strArr[i]) {
                case '{':
                case '[':
                    if (currentItemType == null)
                        currentItemType = strArr[i] == '{' ? itemType.obj : itemType.arr;
                    else if (charsToMatch.size() == 0) {
                        charsToMatch.add(strArr[i]);
                    }
                    break;
                case '}':
                case ']':
                    if ((currentItemType == itemType.obj || currentItemType == itemType.arr) && charsToMatch.size() == 0) {
                        if (strArr[i] == '}') {
                            this.jsonArray.add(new JsonObject(currentItem.toString()));
                        } else {
                            this.jsonArray.add(new JsonArray(currentItem.toString()));
                        }
                        currentItem.delete(0, currentItem.length());
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
                            this.jsonArray.add(currentItem.substring(1, currentItem.length() - 1));
                            currentItem.delete(0, currentItem.length());
                            currentItemType = null;
                        } else if (charsToMatchLastIndex > -1 && charsToMatch.get(charsToMatchLastIndex) == '"') {
                            charsToMatch.remove(charsToMatchLastIndex);
                        } else {
                            charsToMatch.add('"');
                        }
                    }

                    break;
                case ',':
                    if (currentItem.length() <= 1) {
                        currentItem.delete(0, currentItem.length());
                        break;
                    }
                    if (currentItemType == null) {
                        String value = currentItem.substring(0, currentItem.length() - 1);
                        if (CommonServices.isStringInstanceOfBoolean(value)) {
                            jsonArray.add("true".equals(value));
                            currentItem.delete(0, currentItem.length());
                        } else if (CommonServices.isStringInstanceOfNumber(value)) {
                            jsonArray.add(CommonServices.parseStringToNumber(value));
                            currentItem.delete(0, currentItem.length());
                        }
                    }
                    break;
            }

            if (currentItem.length() > 0 && i == strArrEnd - 1 && currentItemType == null) {
                String value = currentItem.toString().trim();
                if (CommonServices.isStringInstanceOfBoolean(value)) {
                    jsonArray.add("true".equals(value));
                    currentItem.delete(0, currentItem.length());
                } else if (CommonServices.isStringInstanceOfNumber(value)) {
                    jsonArray.add(CommonServices.parseStringToNumber(value));
                    currentItem.delete(0, currentItem.length());
                }
            }
        }
    }

    public Object getItemAtIndex(Integer index) {
        return this.jsonArray.get(index);
    }
}
