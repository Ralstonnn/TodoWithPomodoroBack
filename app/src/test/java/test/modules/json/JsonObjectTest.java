package test.modules.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class JsonObjectTest {
  @Test
  public void createJsonObjectFromSingleLine() {
    String testJson = "{\"name\":\"name\",\"data\":{\"surname\":\"surname\",\"age\":21,\"isProgrammer\":true,\"embedded\":{\"value\":\"embedded value\"}}}";
    JsonObject testObject = new JsonObject(testJson);
    assertEquals("name", testObject.getValue(new String[] { "name" }));
    assertEquals("surname", testObject.getValue(new String[] { "data", "surname" }));
    assertEquals(21, testObject.getValue(new String[] { "data", "age" }));
    assertEquals(true, testObject.getValue(new String[] { "data", "isProgrammer" }));
    assertEquals("embedded value", testObject.getValue(new String[] { "data", "embedded", "value" }));
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
    assertEquals("name", testObject.getValue(new String[] { "name" }));
    assertEquals("surname", testObject.getValue(new String[] { "data", "surname" }));
    assertEquals(21, testObject.getValue(new String[] { "data", "age" }));
    assertEquals(true, testObject.getValue(new String[] { "data", "isProgrammer" }));
    assertEquals("embedded value", testObject.getValue(new String[] { "data", "embedded", "value" }));
  }
}
