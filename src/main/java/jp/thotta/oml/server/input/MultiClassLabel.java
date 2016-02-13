package jp.thotta.oml.server.input;

/**
 * 多クラス分類用のラベル.
 */
public class MultiClassLabel implements Label {
  String label = null;

  public void parse(String labelText) {
    label = labelText;
  }

  public int getLabelMode() {
    return LabelFactory.MULTI_MODE;
  }

  public String getLabel() {
    return label;
  }

  public Double getScore() {
    return null;
  }

  public Boolean isPositive() {
    return null;
  }
}
