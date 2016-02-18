package jp.thotta.oml.server.io;

import com.google.gson.Gson;
import junit.framework.TestCase;

public class IODataTest extends TestCase {
  public void testJson() {
    Gson gson = new Gson();
    IOData d1 = new IOData("true", "1:0.1 2:0.6 3:1.0");
    String jsonText = gson.toJson(d1);
    assertEquals(jsonText, "{\"label\":\"true\",\"features\":\"1:0.1 2:0.6 3:1.0\"}");
    IOData d2 = gson.fromJson(jsonText, IOData.class);
    assertEquals(d1, d2);
  }
}
