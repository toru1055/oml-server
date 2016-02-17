package jp.thotta.oml.server.admin;

import junit.framework.TestCase;

public class PathManagerTest extends TestCase {
  public void testPathManager() {
    assertEquals(PathManager.attributesFile(1),
        "/var/db/oml/attributes/1");
    assertEquals(PathManager.modelFile(100),
        "/var/db/oml/model/100");
    PathManager.setBase("data");
    assertEquals(PathManager.attributesFile(1),
        "data/attributes/1");
    assertEquals(PathManager.modelFile(100),
        "data/model/100");
  }
}
