package jp.thotta.oml.server.ml;

import jp.thotta.oml.client.io.*;

/**
 * 線形回帰の抽象クラス.
 */
public abstract class BaseLinearRegression extends BaseBinaryClassifier {
  public BaseLinearRegression(int modelId) {
    super(modelId);
    this.labelMode = LabelFactory.SCORE_MODE;
  }
}
