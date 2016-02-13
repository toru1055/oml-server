package jp.thotta.oml.server.input;

import junit.framework.TestCase;

public class FeatureTest extends TestCase {
  public void testHashKey() {
    Feature f = new Feature("key", 5.5);
    assertEquals(f.key(), "key");
    assertEquals(f.value(), 5.5, 0.001);
    assertEquals(f.hashKey(), "106079");
  }
}
