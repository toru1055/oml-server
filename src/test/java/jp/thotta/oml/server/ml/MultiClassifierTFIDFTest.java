package jp.thotta.oml.server.ml;

import junit.framework.TestCase;
import java.util.ArrayList;
import jp.thotta.oml.server.admin.PathManager;
import jp.thotta.oml.client.io.*;

public class MultiClassifierTFIDFTest extends TestCase {
  int modelId;

  protected void setUp() {
    PathManager.setBase("data");
    PathManager.init();
    Learner learner = LearnerFactory.createLearner(
        LabelFactory.MULTI_MODE_TEXT,
        LearnerFactory.TFIDF_TYPE_TEXT);
    modelId = learner.getModelId();
  }

  public void testLearnerFactory() {
    Learner learner = LearnerFactory.readLearner(modelId);
    MultiClassLabel l1 = new MultiClassLabel();
    l1.parse("label1");
    MultiClassLabel l2 = new MultiClassLabel();
    l2.parse("label2");
    MultiClassLabel l3 = new MultiClassLabel();
    l3.parse("label3");
    ArrayList<Feature> f1 = new ArrayList<Feature>();
    ArrayList<Feature> f2 = new ArrayList<Feature>();
    ArrayList<Feature> f3 = new ArrayList<Feature>();
    f1.add(new Feature("1", 1.0));
    f1.add(new Feature("3", 1.0));
    f1.add(new Feature("5", 1.0));
    f2.add(new Feature("1", 1.0));
    f2.add(new Feature("2", 1.0));
    f2.add(new Feature("3", 1.0));
    f3.add(new Feature("3", 1.0));
    f3.add(new Feature("4", 1.0));
    f3.add(new Feature("5", 1.0));
    learner.train(l1, f1);
    learner.train(l2, f2);
    learner.train(l3, f3);
    learner.save();
    Learner learner2 = LearnerFactory.readLearner(modelId);
    learner2.read();
    assertEquals(learner2.predict(f2).getLabel(), null);
    learner2.train(l1, f2);
    learner2.train(l2, f2);
    learner2.train(l3, f3);
    learner2.save();
  }

  protected void tearDown() {
    LearnerFactory.deleteLearner(modelId);
  }
}
