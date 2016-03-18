package jp.thotta.oml.server.admin;

import java.io.File;
import java.lang.SecurityException;

/**
 * パス管理のUtilityクラス.
 */
public class PathManager {
  static String basePath = "/var/db/oml";

  public static void setBase(String path) {
    basePath = path;
  }

  static boolean initDirectory(File dir) {
    if(dir.exists()) {
      if(dir.isDirectory()) {
        return true;
      } else {
        System.err.println(dir + " is not directory.");
        return false;
      }
    } else {
      if(dir.mkdirs()) {
        return true;
      } else {
        System.err.println(dir + ": Can't make directory.");
        return false;
      }
    }
  }

  public static void init() {
    try {
      File aDir = new File(attributesDirectory());
      File mDir = new File(modelDirectory());
      if(!initDirectory(aDir) || !initDirectory(mDir)) {
        System.exit(1);
      }
    } catch(SecurityException e) {
      e.printStackTrace();
      System.exit(1);
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

  public static String tfFile(int modelId) {
    return modelDirectory() + "/" + String.valueOf(modelId) + ".tf";
  }

  public static String dfFile(int modelId) {
    return modelDirectory() + "/" + String.valueOf(modelId) + ".df";
  }

  public static String thresholdFile(int modelId) {
    return modelDirectory() + "/" + String.valueOf(modelId) + ".threshold";
  }

}
