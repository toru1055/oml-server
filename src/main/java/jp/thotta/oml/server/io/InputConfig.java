package jp.thotta.oml.server.io;

import jp.thotta.oml.server.ml.Learner;
import jp.thotta.oml.server.ml.LearnerFactory;
import jp.thotta.oml.server.parser.FeatureParser;
import jp.thotta.oml.server.parser.FeatureParserFactory;

/**
 * json化するConfig用データ.
 */
public class InputConfig {
  Integer modelId;
  String parserType;

  public InputConfig(Integer modelId, String parserType) {
    this.modelId = modelId;
    this.parserType = parserType;
  }

  public FeatureParser getParser() {
    return FeatureParserFactory.createParser(this.parserType);
  }

  public Learner getLearner() {
    return LearnerFactory.readLearner(modelId);
  }

  @Override
  public boolean equals(Object data) {
    return this.modelId.equals(((InputConfig)data).modelId)
      && this.parserType.equals(((InputConfig)data).parserType);
  }
}
