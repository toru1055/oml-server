package jp.thotta.oml.server.ml;

import jp.thotta.oml.client.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class MultiClassifierTFIDF
  extends BaseLearner implements Learner {
  public static final int TERM_MAX = 100;

  Map<String, Integer> df;
  Map<String, Map<String, Integer>> tf;
  Map<String, Map<String, Double>> invertedIndex;

  public MultiClassifierTFIDF(int modelId) {
    super(modelId);
    this.df = new HashMap<String, Integer>();
    this.tf = new HashMap<String, Map<String, Integer>>();
    this.invertedIndex = new HashMap<String, Map<String, Double>>();
    this.labelMode = LabelFactory.MULTI_MODE;
  }

  /**
   * 数値のついたラベルをMapで取得.
   */
  Map<String, Double> scoring(List<Feature> x) {
    Map<String, Double> scoreMap = new HashMap<String, Double>();
    Map<String, Double> featureMap = normalize(numerize(x), 30);
    for(String term : featureMap.keySet()) {
      Map<String, Double> labelMap = invertedIndex.get(term);
      for(String label : labelMap.keySet()) {
        double val = featureMap.get(term) * labelMap.get(label);
        if(scoreMap.containsKey(label)) {
          scoreMap.put(label, scoreMap.get(label) + val);
        } else {
          scoreMap.put(label, val);
        }
      }
    }
    return scoreMap;
  }

  /**
   * dfを用いた数値化.
   */
  Map<String, Double> numerize(List<Feature> x) {
    Map<String, Double> fmap = new HashMap<String, Double>();
    for(Feature xi : x) {
      double val = xi.value() * idf(xi.key());
      fmap.put(xi.key(), val);
    }
    return fmap;
  }

  /**
   * dfを用いた数値化.
   */
  Map<String, Double> numerize(Map<String, Integer> tfVec) {
    Map<String, Double> fmap = new HashMap<String, Double>();
    for(String term : tfVec.keySet()) {
      double val = tfVec.get(term) * idf(term);
      fmap.put(term, val);
    }
    return fmap;
  }

  /**
   * 長さを指定して正規化.
   */
  Map<String, Double> normalize(Map<String, Double> m, int size) {
    Map<String, Double> nm = new HashMap<String, Double>();
    double norm = 0.0;
    int i = 0;
    List<String> sortedKeys = getSortedKeys(m);
    for(String k : sortedKeys) {
      if(i++ < size) {
        norm += m.get(k) * m.get(k);
      } else {
        break;
      }
    }
    norm = Math.sqrt(norm);
    for(String k : sortedKeys) {
      if(i++ < size) {
        if(norm > 0.0) {
          nm.put(k, m.get(k) / norm);
        } else {
          nm.put(k, 0.0);
        }
      } else {
        break;
      }
    }
    return nm;
  }

  double idf(String term) {
    if(df.containsKey(term)) {
      return Math.log(4000.0 / (1 + df.get(term)));
    } else {
      return Math.log(4000.0);
    }
  }

  /**
   * 値で降順ソート.
   */
  List<String> getSortedKeys(final Map<String, Double> m) {
    List<String> keys = new ArrayList<String>(m.keySet());
    Collections.sort(keys, new Comparator<String>() {
      @Override
      public int compare(String k1, String k2) {
        if(m.get(k1) != m.get(k2)) {
          return m.get(k1) > m.get(k2) ? -1 : 1;
        } else {
          return k1.compareTo(k2);
        }
      }
    });
    return null;
  }

  public void train(Label label, List<Feature> features) {
    // tf, dfを更新
    // scoringで取得したmapで閾値パラメータを更新
  }

  public Label predict(List<Feature> features) {
    // scoringして値が最大のlabelを返す.
    return null;
  }

  public void read() {
    // tf, dfをread
    //  - path: model/$modelId/{tf,df,threshold}
    // tfidfの通常indexを作成.
    // label毎term数の制限と正規化.
    // 転置indexを作成
    // 閾値パラメータをread
  }

  public void save() {
    // readするものをsave
  }

  class Threshold {
    String label;
    Integer posNum;
    Double posMean;
    Double posMin;
    Integer negNum;
    Double negMean;
    Double negMax;

    public Threshold(String label) {
      this.label = label;
    }

    public void set(Integer posNum, Double posMean, Double posMin,
                    Integer negNum, Double negMean, Double negMax) {
      this.posNum = posNum;
      this.posMean = posMean;
      this.posMin = posMin;
      this.negNum = negNum;
      this.negMean = negMean;
      this.negMax = negMax;
    }

    public void addPositive(Double score) {
    }

    public void addNegative(Double score) {
    }

    public Double getThreshold() {
      return null;
    }
  }
}
