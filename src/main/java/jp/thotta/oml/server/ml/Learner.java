package jp.thotta.oml.server.ml;

import jp.thotta.oml.server.io.Feature;
import jp.thotta.oml.server.io.Label;
import java.util.List;

/**
 * 学習器のインターフェイス.
 */
public interface Learner {
  /**
   * モデル番号取得.
   */
  public int getModelId();

  /**
   * １レコード学習.
   * @param label 教師ラベル
   * @param features 特徴ベクトル
   */
  public void train(Label label, List<Feature> features);

  /**
   * 未知の特徴ベクトルのラベルを推定.
   * @param features 特徴ベクトル
   */
  public Label predict(List<Feature> features);

  /**
   * 未知の特徴ベクトルのスコアを算出.
   * @param features 特徴ベクトル
   */
  public Double scoring(List<Feature> features);

  /**
   * 学習モデルを保存.
   */
  public void save();

  /**
   * 学習モデルを読み込み.
   */
  public void read();

  /**
   * 学習器に対応するラベルインスタンスを生成.
   */
  public Label createLabelInstance();
}
