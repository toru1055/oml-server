package jp.thotta.oml.server.ml;

import jp.thotta.oml.client.io.*;

import java.util.List;

/**
 * SGDで線形回帰を学習・推定する学習器.
 * http://www.ms.k.u-tokyo.ac.jp/2014/OnlineLearningReview-jp.pdf
 */
public class LinearRegressionSGD extends BaseLinearRegression implements Learner {
    double eta = 0.025;

    public LinearRegressionSGD(int modelId) {
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
        return wx;
    }

    public Label predict(List<Feature> x) {
        double p = scoring(x);
        ScoreLabel l = new ScoreLabel();
        l.setScore(p);
        return l;
    }

    public void train(Label label, List<Feature> x) {
        double y = label.getScore();
        double p = scoring(x);
        for (Feature xi : x) {
            String k = xi.key();
            Double v = xi.value();
            Double wi = wget(k);
            wi = wi - (eta * (p - y) * v);
            w.put(k, wi);
        }
    }
}
