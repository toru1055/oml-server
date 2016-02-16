package jp.thotta.oml.server.ml;

import junit.framework.TestCase;
import jp.thotta.oml.server.input.LabelFactory;
import jp.thotta.oml.server.admin.PathManager;

public class LearnerTest extends TestCase {
  public void testLearnerFactory() {
    PathManager.init();
    Learner learner = LearnerFactory.createLearner(
        LabelFactory.BINARY_MODE_TEXT,
        LearnerFactory.SGD_TYPE_TEXT);
    int modelId = learner.getModelId();
    Learner l2 = LearnerFactory.readLearner(modelId);
    //TODO: データを作って学習してみる.
  }
}
