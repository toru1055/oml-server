package jp.thotta.oml.server.parser;

/**
 * FeatureParserを生成するクラス.
 */
public class FeatureParserFactory {
  public static final String BINARY_MA_TYPE = "ma";
  public static final String LIBSVM_TYPE = "libsvm";

  /**
   * 入力されたタイプに合わせてパーサを生成.
   * @param parserType パーサーのタイプ
   */
  public static FeatureParser createParser(String parserType) {
    if(BINARY_MA_TYPE.equals(parserType)) {
      return new FeatureParserBinaryMA();
    } else if(LIBSVM_TYPE.equals(parserType)) {
      return null;
    } else {
      return null;
    }
  }
}
