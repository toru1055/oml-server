package jp.thotta.oml.server.io;

/**
 * 特徴量のクラス.
 */
public class Feature {
  static int HASH_SIZE = (int)Math.pow(2.0, 24);
  String k;
  Double v;

  public Feature(String k, Double v) {
    this.k = k;
    this.v = v;
  }

  public String hashKey() {
    int hash = k.hashCode();
    return String.valueOf((hash & 0x7FFFFFFF) % HASH_SIZE);
  }

  public String key() {
    return k;
  }

  public Double value() {
    return v;
  }

  public String toString() {
    return String.format("%s:%f", k, v);
  }
}
