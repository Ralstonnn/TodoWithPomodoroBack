package test.modules.json;

import java.sql.Array;
import java.util.ArrayList;

public class JsonArray {
    private ArrayList<Object> jsonArray = new ArrayList<>();
    public JsonArray(Object[] objects) {
        for (Object obj:objects) {
            if (obj instanceof Boolean || obj instanceof Number || obj instanceof String) {
                this.jsonArray.add(obj);
            } else if (obj instanceof Array) {
                this.jsonArray.add(new JsonArray((Object[]) obj));
            } else {
                this.jsonArray.add(new JsonObject(obj));
            }
        }
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

            if (i < lenght - 1) result.append(",");
        }
        result.append("]");
        return result.toString();
    }

    public Object getItemAtIndex(Integer index) {
        return this.jsonArray.get(index);
    }
}
