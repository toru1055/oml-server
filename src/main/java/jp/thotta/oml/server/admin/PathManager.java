package jp.thotta.oml.server.admin;

import java.io.File;

/**
 * パス管理のUtilityクラス.
 */
public class PathManager {
  static String basePath = "/var/db/oml";

  public static void setBase(String path) {
    basePath = path;
  }

  public static void init() {
    try {
      File aDir = new File(attributesDirectory());
      File mDir = new File(modelDirectory());
      aDir.mkdirs();
      mDir.mkdirs();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static String baseDirectory() {
    return basePath;
  }

  public static String attributesDirectory() {
    return baseDirectory() + "/" + "attributes";
  }

  public static String attributesFile(int modelId) {
    return attributesDirectory() + "/" + String.valueOf(modelId);
  }

  public static String modelDirectory() {
    return baseDirectory() + "/" + "model";
  }

  public static String modelFile(int modelId) {
    return modelDirectory() + "/" + String.valueOf(modelId);
  }
}
