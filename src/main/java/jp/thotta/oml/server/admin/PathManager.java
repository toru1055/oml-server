package jp.thotta.oml.server.admin;

/**
 * パス管理のUtilityクラス.
 */
public class PathManager {
  public static void init() {
    // ディレクトリ作成やファイル作成.
  }

  public static String baseDirectory() {
    return "data";
  }

  public static String attributesDirectory() {
    return baseDirectory() + "/" + "attributes";
  }

  public static String attributesFile(int modelId) {
    return attributesDirectory() + "/" +
      String.valueOf(modelId) +
      ".properties";
  }

  public static String modelDirectory() {
    return baseDirectory() + "/" + "model";
  }

  public static String modelFile(int modelId) {
    return modelDirectory() + "/" + String.valueOf(modelId) +
      ".properties";
  }

  public static String modelIdManagerFile() {
    return baseDirectory() + "/" + "model-id.properties";
  }
}
