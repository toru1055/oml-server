package jp.thotta.oml.server.ml;

import junit.framework.TestCase;
import java.util.ArrayList;
import jp.thotta.oml.server.admin.PathManager;
import jp.thotta.oml.server.input.*;

public class LearnerTest extends TestCase {
  int modelId;
  protected void setUp() {
    PathManager.init();
    Learner learner = LearnerFactory.createLearner(
        LabelFactory.BINARY_MODE_TEXT,
        LearnerFactory.SGD_TYPE_TEXT);
    modelId = learner.getModelId();
  }

  public void testLearnerFactory() {
    Learner l2 = LearnerFactory.readLearner(modelId);
    BinaryClassLabel p = new BinaryClassLabel();
    BinaryClassLabel n = new BinaryClassLabel();
    p.setLabel(true);
    n.setLabel(false);
    for(int i = 0; i < 100; i++) {
      ArrayList<Feature> pos = new ArrayList<Feature>();
      pos.add(new Feature("1", 1.0));
      pos.add(new Feature("2", Math.random()));
      pos.add(new Feature("3", Math.random()));
      ArrayList<Feature> neg = new ArrayList<Feature>();
      neg.add(new Feature("1", Math.random()));
      neg.add(new Feature("2", Math.random()));
      neg.add(new Feature("3", 1.0));
      l2.train(p, pos);
      l2.train(n, neg);
    }
    l2.save();
    Learner l3 = LearnerFactory.readLearner(modelId);
    l3.read();
    ArrayList<Feature> r1 = new ArrayList<Feature>();
    r1.add(new Feature("1", 1.0));
    r1.add(new Feature("2", 0.5));
    r1.add(new Feature("3", 0.0));
    assertTrue(l3.predict(r1).isPositive());
    ArrayList<Feature> r2 = new ArrayList<Feature>();
    r2.add(new Feature("1", 0.0));
    r2.add(new Feature("2", 0.5));
    r2.add(new Feature("3", 1.0));
    assertTrue(!l3.predict(r2).isPositive());
  }

  protected void tearDown() {
    LearnerFactory.deleteLearner(modelId);
  }
}
