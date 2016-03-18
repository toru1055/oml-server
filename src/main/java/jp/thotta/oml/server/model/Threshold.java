package jp.thotta.oml.server.model;

import java.util.Map;

public class Threshold {
  public String label;
  Integer posNum = 0;
  Double posMean = 0.0;
  Double posMin = 0.0;
  Integer negNum = 0;
  Double negMean = 0.0;
  Double negMax = 0.0;

  public Threshold() {
    this.label = null;
  }

  public Threshold(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public void readLine(String line) {
    String[] s = line.split("\t", 7);
    label = s[0];
    posNum = Integer.parseInt(s[1]);
    posMean = Double.parseDouble(s[2]);
    posMin = Double.parseDouble(s[3]);
    negNum = Integer.parseInt(s[4]);
    negMean = Double.parseDouble(s[5]);
    negMax = Double.parseDouble(s[6]);
  }

  public String getLine() {
    return label + "\t" +
      posNum + "\t" +
      posMean + "\t" +
      posMin + "\t" +
      negNum + "\t" +
      negMean + "\t" +
      negMax;
  }

  public void addScoreMap(Map<String, Double> m) {
    for(String l : m.keySet()) {
      Double score = m.get(l);
      if(label.equals(l)) {
        addPositive(score);
      } else {
        addNegative(score);
      }
    }
  }

  void addPositive(Double score) {
    posMean = (posMean * posNum + score) / (posNum + 1);
    posMin = (posMin < score) ? posMin : score;
    posNum = posNum + 1;
  }

  void addNegative(Double score) {
    negMean = (negMean * negNum + score) / (negNum + 1);
    negMax = (negMax > score) ? negMax : score;
    negNum = negNum + 1;
  }

  public Double getThreshold() {
    return posMean;
  }
}
