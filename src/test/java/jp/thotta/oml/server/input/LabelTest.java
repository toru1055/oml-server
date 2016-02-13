package jp.thotta.oml.server.input;

import junit.framework.TestCase;

public class LabelTest extends TestCase {

  public void testBinaryClassLabel() {
    BinaryClassLabel p = new BinaryClassLabel();
    BinaryClassLabel n = new BinaryClassLabel();
    p.parse(BinaryClassLabel.POSITIVE_LABEL);
    n.parse(BinaryClassLabel.NEGATIVE_LABEL);
    assertEquals(p.getLabel(), BinaryClassLabel.POSITIVE_LABEL);
    assertEquals(n.getLabel(), BinaryClassLabel.NEGATIVE_LABEL);
    assertEquals(p.getScore(), 1.0, 0.001);
    assertEquals(n.getScore(), 0.0, 0.001);
    assertTrue(p.isPositive());
    assertFalse(n.isPositive());
  }

  public void testMultiClassLabel() {
    MultiClassLabel c1 = new MultiClassLabel();
    MultiClassLabel c2 = new MultiClassLabel();
    MultiClassLabel c3 = new MultiClassLabel();
    c1.parse("c1");
    c2.parse("c2");
    c3.parse("c3");
    assertEquals(c1.getLabel(), "c1");
    assertEquals(c2.getLabel(), "c2");
    assertEquals(c3.getLabel(), "c3");
    assertEquals(c1.getScore(), null);
    assertEquals(c1.isPositive(), null);
  }

  public void testScoreLabel() {
    ScoreLabel s1 = new ScoreLabel();
    ScoreLabel s2 = new ScoreLabel();
    ScoreLabel s3 = new ScoreLabel();
    s1.parse("2.5");
    s2.parse("-0.5");
    s3.parse("+1");
    assertEquals(s1.getScore(), 2.5, 0.001);
    assertEquals(s2.getScore(), -0.5, 0.001);
    assertEquals(s3.getScore(), 1.0, 0.001);
    assertEquals(s1.getLabel(), null);
    assertTrue(s1.isPositive());
    assertFalse(s2.isPositive());
    assertTrue(s3.isPositive());
  }

  public void testLabelFactory() {
    Label bl = LabelFactory.createLabel(LabelFactory.BINARY_MODE);
    assertEquals(bl.getLabelMode(), LabelFactory.BINARY_MODE);
  }

}
