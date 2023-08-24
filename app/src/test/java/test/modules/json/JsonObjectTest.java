package test.modules.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JsonObjectTest {
    @Test
    public void createJsonObjectFromSingleLine() {
        String testJson = "{\"name\":\"name\",\"data\":{\"surname\":\"surname\",\"age\":21,\"isProgrammer\":true,\"embedded\":{\"value\":\"embedded value\"}}}";
        JsonObject testObject = new JsonObject(testJson);
        assertEquals("name", testObject.getValue(new String[]{"name"}));
        assertEquals("surname", testObject.getValue(new String[]{"data", "surname"}));
        assertEquals(21, testObject.getValue(new String[]{"data", "age"}));
        assertEquals(true, testObject.getValue(new String[]{"data", "isProgrammer"}));
        assertEquals("embedded value", testObject.getValue(new String[]{"data", "embedded", "value"}));
    }

    @Test
    public void createJsonObjectFromMultipleLines() {
        String testJson = "{\n" + //
                "    \"name\": \"name\",\n" + //
                "    \"data\": {\n" + //
                "        \"surname\": \"surname\",\n" + //
                "        \"age\": 21,\n" + //
                "        \"isProgrammer\": true,\n" + //
                "        \"embedded\": {\n" + //
                "            \"value\": \"embedded value\"\n" + //
                "        }\n" + //
                "    }\n" + //
                "}";
        JsonObject testObject = new JsonObject(testJson);
        assertEquals("name", testObject.getValue(new String[]{"name"}));
        assertEquals("surname", testObject.getValue(new String[]{"data", "surname"}));
        assertEquals(21, testObject.getValue(new String[]{"data", "age"}));
        assertEquals(true, testObject.getValue(new String[]{"data", "isProgrammer"}));
        assertEquals("embedded value", testObject.getValue(new String[]{"data", "embedded", "value"}));
    }

    @Test
    public void createJsonFormattedArray() {
        String testJson = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"is_done\": {\n" +
                "      \"value\": true\n" +
                "    },\n" +
                "    \"item\": {\n" +
                "      \"text\": \"something }in here to test post\",\n" +
                "      \"allGood\": true\n" +
                "    }\n" +
                "  },\n" +
                "  [1, 2, 3, 4],\n" +
                "  false,\n" +
                "  \"some text in here ]\",\n" +
                "  [\"array with text ] \", \"other item\"],\n" +
                "  \"another [string]\",\n" +
                "  45\n" +
                "]";
        JsonArray ja = new JsonArray(testJson);
        assertEquals(1, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"id"}));
        assertEquals(true, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"is_done", "value"}));
        assertEquals("something }in here to test post", ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"item", "text"}));
        assertEquals(true, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"item", "allGood"}));
        assertEquals(1, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(0));
        assertEquals(2, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(1));
        assertEquals(3, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(2));
        assertEquals(4, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(3));
        assertEquals(false, ja.getItemAtIndex(2));
        assertEquals("some text in here ]", ja.getItemAtIndex(3));
        assertEquals("array with text ] ", ((JsonArray) ja.getItemAtIndex(4)).getItemAtIndex(0));
        assertEquals("other item", ((JsonArray) ja.getItemAtIndex(4)).getItemAtIndex(1));
        assertEquals("another [string]", ja.getItemAtIndex(5));
        assertEquals(45, ja.getItemAtIndex(6));
    }

    @Test
    public void createJsonSingleStringArray() {
        String testJson = "[{\"id\":1,\"is_done\":{\"value\":true},\"item\":{\"text\":\"something in here to test post\",\"allGood\":true}},[1, 2, 3, 4],false,\"some text in here ]\",[\"array with text ] \",\"other item\"],\"another [string]\",45]";
        JsonArray ja = new JsonArray(testJson);
        assertEquals(1, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"id"}));
        assertEquals(true, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"is_done", "value"}));
        assertEquals("something in here to test post", ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"item", "text"}));
        assertEquals(true, ((JsonObject) ja.getItemAtIndex(0)).getValue(new String[]{"item", "allGood"}));
        assertEquals(1, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(0));
        assertEquals(2, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(1));
        assertEquals(3, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(2));
        assertEquals(4, ((JsonArray) ja.getItemAtIndex(1)).getItemAtIndex(3));
        assertEquals(false, ja.getItemAtIndex(2));
        assertEquals("some text in here ]", ja.getItemAtIndex(3));
        assertEquals("array with text ] ", ((JsonArray) ja.getItemAtIndex(4)).getItemAtIndex(0));
        assertEquals("other item", ((JsonArray) ja.getItemAtIndex(4)).getItemAtIndex(1));
        assertEquals("another [string]", ja.getItemAtIndex(5));
        assertEquals(45, ja.getItemAtIndex(6));
    }
}
