package jp.thotta.oml.server.ml;

import jp.thotta.oml.client.io.*;

import java.util.List;

/**
 * SGDで２クラス分類を学習・推定する学習器.
 */
public class BinaryClassierSGD extends BaseBinaryClassifier implements Learner {
    double eta = 0.1;

    public BinaryClassierSGD(int modelId) {
        super(modelId);
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    Double scoring(List<Feature> x) {
        double wx = 0.0;
        for (Feature xi : x) {
            wx += wget(xi.key()) * xi.value();
        }
        return sigmoid(wx);
    }

    public Label predict(List<Feature> x) {
        double p = scoring(x);
        BinaryClassLabel l = new BinaryClassLabel();
        if (p > 0.5) {
            l.setLabel(true);
        } else {
            l.setLabel(false);
        }
        return l;
    }

    public void train(Label label, List<Feature> x) {
        double y = label.isPositive() ? 1.0 : 0.0;
        double p = scoring(x);
        for (Feature xi : x) {
            String k = xi.key();
            Double v = xi.value();
            Double wi = wget(k);
            wi = wi - (eta * (p - y) * v);
            w.put(k, wi);
        }
    }

    static double sigmoid(double a) {
        return 1.0 / (1 + Math.exp(-a));
    }
}
