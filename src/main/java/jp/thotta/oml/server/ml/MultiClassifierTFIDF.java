package jp.thotta.oml.server.ml;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import jp.thotta.oml.client.io.*;
import jp.thotta.oml.server.admin.PathManager;
import jp.thotta.oml.server.model.*;

public class MultiClassifierTFIDF
  extends BaseLearner implements Learner {
  public static final int INDEX_TERM_MAX = 30;
  public static final int QUERY_TERM_MAX = 10;
  public static final String TOTAL_DOC_SIZE = "\001TOTAL_DOCUMENT_SIZE\001";

  DocumentFrequency df;
  TermFrequency tf;
  ThresholdMap thresholdMap;
  Map<String, Map<String, Double>> invertedIndex;

  public MultiClassifierTFIDF(int modelId) {
    super(modelId);
    this.df = new DocumentFrequency(modelId);
    this.tf = new TermFrequency(modelId);
    this.thresholdMap = new ThresholdMap(modelId);
    this.invertedIndex = new HashMap<String, Map<String, Double>>();
    this.labelMode = LabelFactory.MULTI_MODE;
    this.df.put(TOTAL_DOC_SIZE, 0);
  }

  /**
   * 数値のついたラベルをMapで取得.
   */
  Map<String, Double> scoring(List<Feature> x) {
    Map<String, Double> scoreMap = new HashMap<String, Double>();
    Map<String, Double> featureMap = normalize(numerize(x), QUERY_TERM_MAX);
    for(String term : featureMap.keySet()) {
      if(invertedIndex.containsKey(term)) {
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
    i = 0;
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
    double idf = 0.0;
    int df_max = 2;
    if(df.get(TOTAL_DOC_SIZE) > df_max) {
      df_max = df.get(TOTAL_DOC_SIZE);
    }
    if(df.containsKey(term)) {
      if((df.get(term) + 1) > df_max) {
        df_max = 1 + df.get(term);
      }
      idf = Math.log((double)df_max / (1 + df.get(term)));
    } else {
      idf = Math.log((double)df_max);
    }
    if(idf < 0.0) {
      System.err.println("idf is minus: term = " + term + ", df = " + df.get(term));
      System.err.println("df_max " + df_max);
      return 0.0;
    } else {
      return idf;
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
        if(!m.get(k1).equals(m.get(k2))) {
          return m.get(k1) > m.get(k2) ? -1 : 1;
        } else {
          return k2.compareTo(k1);
        }
      }
    });
    return keys;
  }

  public void train(Label label, List<Feature> features) {
    String l = label.getLabel();
    Map<String, Double> scoreMap = scoring(features);
    Threshold threshold;
    if(thresholdMap.containsKey(l)) {
      threshold = thresholdMap.get(l);
    } else {
      threshold = new Threshold(l);
      thresholdMap.put(l, threshold);
    }
    threshold.addScoreMap(scoreMap);
    Map<String, Integer> labelTF;
    if(tf.containsKey(l)) {
      labelTF = tf.get(l);
    } else {
      labelTF = new HashMap<String, Integer>();
      df.put(TOTAL_DOC_SIZE, df.get(TOTAL_DOC_SIZE) + 1);
    }
    for(Feature f : features) {
      String term = f.key();
      int freq = (int)(double)f.value();
      if(labelTF.containsKey(term)) {
        freq = freq + labelTF.get(term);
      } else {
        if(df.containsKey(term)) {
          df.put(term, df.get(term) + 1);
        } else {
          df.put(term, 1);
        }
      }
      labelTF.put(term, freq);
    }
    tf.put(l, labelTF);
  }

  public Label predict(List<Feature> features) {
    Map<String, Double> scoreMap = scoring(features);
    double maxScore = 0.0;
    String maxLabel = null;
    for(String l : scoreMap.keySet()) {
      Double score = scoreMap.get(l);
      if(score > maxScore) {
        maxScore = score;
        maxLabel = l;
      }
    }
    MultiClassLabel label = new MultiClassLabel();
    label.parse(maxLabel);
    return label;
  }

  public void read() {
    tf.read();
    df.read();
    thresholdMap.read();
    for(String l: tf.keySet()) {
      Map<String, Double> tfidf = normalize(numerize(tf.get(l)), INDEX_TERM_MAX);
      for(String term : tfidf.keySet()) {
        Double value = tfidf.get(term);
        Map<String, Double> labelVector = null;
        if(invertedIndex.containsKey(term)) {
          labelVector = invertedIndex.get(term);
        } else {
          labelVector = new HashMap<String, Double>();
          invertedIndex.put(term, labelVector);
        }
        labelVector.put(l, value);
      }
    }
  }

  public void save() {
    tf.save();
    df.save();
    thresholdMap.save();
  }
}
