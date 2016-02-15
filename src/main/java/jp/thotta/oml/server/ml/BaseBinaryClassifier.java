package jp.thotta.oml.server.ml;

import java.util.Properties;
import java.util.HashMap;
import java.util.List;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import jp.thotta.oml.server.input.Feature;
import jp.thotta.oml.server.input.Label;
import jp.thotta.oml.server.input.LabelFactory;
import jp.thotta.oml.server.admin.PathManager;

/**
 * ２クラス分類の抽象クラス.
 */
public abstract class BaseBinaryClassifier extends BaseLearner {
  protected HashMap<String, Double> w;

  public BaseBinaryClassifier(int modelId) {
    super(modelId);
    this.w = new HashMap<String, Double>();
    this.labelMode = LabelFactory.BINARY_MODE;
  }

  /**
   * 特徴ベクトルに対応する重みベクトルを初期化.
   * 空の重みのみをランダムで埋める.
   */
  protected void initWeights(List<Feature> features) {
    for(Feature f : features) {
      String k = f.key();
      if(!w.containsKey(k)) {
        w.put(k, Math.random());
      }
    }
  }

  public void read() {
    String filename = PathManager.modelFile(this.modelId);
    Properties p = new Properties();
    try {
      p.load(new FileInputStream(filename));
    } catch(Exception e) {
      System.err.println("Cannot open " + filename + ".");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void save() {
    String filename = PathManager.modelFile(this.modelId);
    Properties p = new Properties();
    for(String k : w.keySet()) {
      p.setProperty(k, String.valueOf(w.get(k)));
    }
    try {
      p.store(new FileOutputStream(filename),
          "BaseBinaryClassifier.save");
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
