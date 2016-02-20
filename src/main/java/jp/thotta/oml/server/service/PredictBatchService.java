package jp.thotta.oml.server.service;

import java.net.Socket;
import java.util.List;
import java.lang.Runnable;
import jp.thotta.oml.server.io.*;

/**
 * バッチ処理で予測するサービス.
 */
public class PredictBatchService extends BatchService implements Runnable {
  public static final int PORT = 9002;

  public PredictBatchService(Socket socket) {
    super(socket);
  }

  public Label exec(Label label, List<Feature> x) {
    return this.learner.predict(x);
  }

  public void finalizeService() {
    return;
  }
}
