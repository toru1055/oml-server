package jp.thotta.oml.server.io;

import com.google.gson.Gson;
import junit.framework.TestCase;

public class IODataTest extends TestCase {
  public void testIOData() {
    Gson gson = new Gson();
    IOData d1 = new IOData("true", "1:0.1 2:0.6 3:1.0");
    String jsonText = gson.toJson(d1);
    assertEquals(jsonText, "{\"label\":\"true\",\"features\":\"1:0.1 2:0.6 3:1.0\"}");
    IOData d2 = gson.fromJson(jsonText, IOData.class);
    assertEquals(d1, d2);
  }

  public void testInputConfig() {
    Gson gson = new Gson();
    InputConfig c1 = new InputConfig(11, "ma");
    String jsonText = gson.toJson(c1);
    assertEquals(jsonText, "{\"modelId\":11,\"parserType\":\"ma\"}");
    InputConfig c2 = gson.fromJson(jsonText, InputConfig.class);
    assertEquals(c1, c2);
  }
}
