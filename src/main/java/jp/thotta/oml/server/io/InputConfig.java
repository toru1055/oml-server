package jp.thotta.oml.server.io;

/**
 * json化するConfig用データ.
 */
public class InputConfig {
  Integer modelId;
  String parserType;

  public InputConfig(Integer modelId, String parserType) {
    this.modelId = modelId;
    this.parserType = parserType;
  }

  @Override
  public boolean equals(Object data) {
    return this.modelId.equals(((InputConfig)data).modelId)
      && this.parserType.equals(((InputConfig)data).parserType);
  }
}
