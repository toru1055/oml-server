package jp.thotta.oml.server.ml;

import jp.thotta.oml.server.input.Feature;
import jp.thotta.oml.server.input.Label;
import jp.thotta.oml.server.input.BinaryClassLabel;
import java.util.List;

/**
 * SGDで２クラス分類を学習・推定する学習器.
 */
public class BinaryClassierSGD
  extends BaseBinaryClassifier
  implements Learner {
  double eta = 0.1;

  public BinaryClassierSGD(int modelId) {
    super(modelId);
  }

  public void setEta(double eta) {
    this.eta = eta;
  }

  public Double scoring(List<Feature> x) {
    double wx = 0.0;
    for(Feature xi : x) {
      wx += w.get(xi.key()) * xi.value();
    }
    return sigmoid(wx);
  }

  public Label predict(List<Feature> x) {
    double p = scoring(x);
    BinaryClassLabel l = new BinaryClassLabel();
    if(p > 0.5) {
      l.parse(BinaryClassLabel.POSITIVE_LABEL);
    } else {
      l.parse(BinaryClassLabel.NEGATIVE_LABEL);
    }
    return l;
  }

  public void train(Label label, List<Feature> x) {
    initWeights(x);
    int y = label.isPositive()? 1 : 0;
    double p = scoring(x);
    for(Feature xi : x) {
      String k = xi.key();
      Double v = xi.value();
      v -= (eta * (p - y) * v);
      w.put(k, v);
    }
  }

  static double sigmoid(double a) {
    return 1.0 / (1 + Math.exp(-a));
  }
}
