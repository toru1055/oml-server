package jp.thotta.oml.server.io;

/**
 * ラベルを生成するクラス.
 */
public class LabelFactory {
  public static final int BINARY_MODE = 1;
  public static final int MULTI_MODE = 2;
  public static final int SCORE_MODE = 3;
  public static final String BINARY_MODE_TEXT = "binary";
  public static final String MULTI_MODE_TEXT = "multi";
  public static final String SCORE_MODE_TEXT = "score";

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

  public static Integer convertModeId(String modeText) {
    if(BINARY_MODE_TEXT.equals(modeText)) {
      return BINARY_MODE;
    } else if(MULTI_MODE_TEXT.equals(modeText)) {
      return MULTI_MODE;
    } else if(SCORE_MODE_TEXT.equals(modeText)) {
      return SCORE_MODE;
    } else {
      return null;
    }
  }
}
