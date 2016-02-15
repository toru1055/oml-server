package jp.thotta.oml.server.ml;

import junit.framework.TestCase;
import jp.thotta.oml.server.input.LabelFactory;

public class LearnerTest extends TestCase {
  public void testLearnerFactory() {
    Learner learner = LearnerFactory.createLearner(
        LabelFactory.BINARY_MODE_TEXT,
        LearnerFactory.SGD_TYPE_TEXT);
  }
}
