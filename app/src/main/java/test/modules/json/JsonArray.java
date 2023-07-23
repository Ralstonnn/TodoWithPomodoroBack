package test.modules.json;

import java.util.ArrayList;

public class JsonArray {
    private ArrayList<JsonObject> jsonArray = new ArrayList<>();
    public JsonArray(Object[] objects) {
        for (Object obj:objects) {
            jsonArray.add(new JsonObject(obj));
        }
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0, lenght = this.jsonArray.size(); i < lenght; i++) {
            result.append(this.jsonArray.get(i).toString());
            if (i < lenght - 1) result.append(",");
        }
        result.append("]");
        return result.toString();
    }
}
