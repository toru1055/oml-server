package jp.thotta.oml.server.io;

/**
 * json化する入出力用データ.
 */
public class IOData {
  String label;
  String features;

  public IOData(String label, String features) {
    this.label = label;
    this.features = features;
  }

  @Override
  public boolean equals(Object data) {
    return this.label.equals(((IOData)data).label)
      && this.features.equals(((IOData)data).features);
  }
}
