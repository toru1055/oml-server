package jp.thotta.oml.server.ml;

import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
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
    File file = new File(filename);
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = "";
      while ((line = br.readLine()) != null) {
        String[] s = line.split("\t", 2);
        String k = s[0];
        Double v = Double.parseDouble(s[1]);
        this.w.put(k, v);
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void save() {
    String filename = PathManager.modelFile(this.modelId);
    File file = new File(filename);
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      for(String k : this.w.keySet()) {
        bw.write(k + "\t" + this.w.get(k));
        bw.newLine();
      }
      bw.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}