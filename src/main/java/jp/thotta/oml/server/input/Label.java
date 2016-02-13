package jp.thotta.oml.server.input;

/**
 * 教師データのラベル用インターフェイス.
 * ２クラス、多クラス、回帰用など、それぞれに実装を用意する.
 */
public interface Label {
  /**
   * 文字列からラベルを読み込む.
   * @param labelText ラベルの文字列
   */
  public void parse(String labelText);

  /**
   * ラベルが表すクラスを取得.
   */
  public String getLabel();

  /**
   * ラベルが表すスコアを取得.
   */
  public Double getScore();

  /**
   * ラベルが正例かを取得.
   */
  public Boolean isPositive();

  /**
   * ラベルタイプを取得.
   */
  public int getLabelMode();
}
