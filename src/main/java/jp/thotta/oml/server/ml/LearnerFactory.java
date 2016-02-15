package jp.thotta.oml.server.ml;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.HashMap;
import jp.thotta.oml.server.input.LabelFactory;
import jp.thotta.oml.server.admin.PathManager;

/**
 * 学習器を生成するクラス.
 */
public class LearnerFactory {
  public static final int SGD_TYPE = 1;
  public static final String SGD_TYPE_TEXT = "sgd";
  static final String CONF_KEY_LABEL = "label_mode";
  static final String CONF_KEY_LEARNER = "learner_type";

  /**
   * 学習器を生成.
   * 同時にモデル番号発番とディレクトリ作成と設定保存も
   */
  public static Learner createLearner(String labelModeText,
                                      String learnerTypeText) {
    // model番号発番
    int modelId = newModelId();
    // StringからlabelMode番号に変換
    int labelMode = LabelFactory.convertModeId(labelModeText);
    // StringからlearnerType番号に変換
    int learnerType = convertTypeId(learnerTypeText);
    // labelMode番号とlearnerType番号からLearnerのインスタンス作成
    Learner learner = newLearner(modelId, labelMode, learnerType);
    // 属性ファイル名を決定し、保存
    saveAttributes(modelId, labelMode, learnerType);
    // model管理ファイルに保存
    saveModelIdManagerFile(modelId);
    return learner;
  }

  /**
   * 学習器の読み込み.
   * 設定の読み込みとLearnerのインスタンス生成
   */
  public static Learner readLearner(int modelId) {
    int labelMode;
    int learnerType;
    //readAttributes
    String filename = PathManager.attributesFile(modelId);
    Properties conf = new Properties();
    try {
      conf.load(new FileInputStream(filename));
    } catch(Exception e) {
      System.err.println("Cannot open " + filename + ".");
      e.printStackTrace();
      System.exit(1);
    }
    labelMode = Integer.parseInt(conf.getProperty(CONF_KEY_LABEL));
    learnerType = Integer.parseInt(conf.getProperty(CONF_KEY_LEARNER));
    return newLearner(modelId, labelMode, learnerType);
  }

  static void saveAttributes(int modelId, 
                             int labelMode,
                             int learnerType) {
    String filename = PathManager.attributesFile(modelId);
    Properties conf = new Properties();
    conf.setProperty(CONF_KEY_LABEL, String.valueOf(labelMode));
    conf.setProperty(CONF_KEY_LEARNER, String.valueOf(learnerType));
    try {
      conf.store(new FileOutputStream(filename),
          "LearnerFactory.saveAttributes");
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  static void saveModelIdManagerFile(int modelId) {
    String filename = PathManager.modelIdManagerFile();
    Properties conf = new Properties();
    conf.setProperty(String.valueOf(modelId), "active");
    try {
      conf.store(new FileOutputStream(filename),
          "LearnerFactory.saveModelIdManagerFile");
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  static Learner newLearner(int modelId,
                            int labelMode,
                            int learnerType) {
    if(labelMode == LabelFactory.BINARY_MODE) {
      if(learnerType == SGD_TYPE) {
        return new BinaryClassierSGD(modelId);
      }
    }
    return null;
  }

  static Integer convertTypeId(String learnerType) {
    HashMap<String, Integer> m = new HashMap<String, Integer>();
    m.put(SGD_TYPE_TEXT, SGD_TYPE);
    return m.get(learnerType);
  }

  static int newModelId() {
    int newId = 0;
    String filename = PathManager.modelIdManagerFile();
    Properties conf = new Properties();
    try {
      conf.load(new FileInputStream(filename));
      for(String k : conf.stringPropertyNames()) {
        int kid = Integer.parseInt(k);
        if(kid > newId) {
          newId = kid;
        }
      }
    } catch(Exception e) {
      System.err.println("Cannot open " + filename + ".");
      e.printStackTrace();
      System.exit(1);
    }
    return (newId + 1);
  }
}
