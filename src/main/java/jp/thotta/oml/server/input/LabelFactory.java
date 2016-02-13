package jp.thotta.oml.server.input;

/**
 * ラベルを生成するクラス.
 */
public class LabelFactory {
  public static int BINARY_MODE = 1;
  public static int MULTI_MODE = 2;
  public static int SCORE_MODE = 3;

  public static Label createLabel(int labelMode) {
    if(labelMode == BINARY_MODE) {
      return new BinaryClassLabel();
    } else if(labelMode == MULTI_MODE) {
      return new MultiClassLabel();
    } else if(labelMode == SCORE_MODE) {
      return new ScoreLabel();
    } else {
      return null;
    }
  }
}
