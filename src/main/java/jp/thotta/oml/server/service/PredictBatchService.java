package jp.thotta.oml.server.service;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.lang.Runnable;
import jp.thotta.oml.server.io.*;

/**
 * バッチ処理で予測するサービス.
 */
public class PredictBatchService extends BatchService implements Runnable {
  public PredictBatchService(Socket socket) {
    super(socket);
  }

  protected boolean configureServiceName() {
    return true;
  }

  protected void executeService() throws IOException {
    while(true) {
      String featuresText = comm.recvFeatures();
      if(featuresText != null) {
        List<Feature> x = this.parser.parse(featuresText);
        Label label = this.learner.predict(x);
        this.comm.sendLabel(label.getText());
      } else {
        break;
      }
    }
  }

  protected void finalizeService() {
    return;
  }
}
