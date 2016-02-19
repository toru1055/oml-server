package jp.thotta.oml.server.service;

import java.util.List;
import java.lang.Runnable;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import jp.thotta.oml.server.io.*;
import jp.thotta.oml.server.ml.Learner;
import jp.thotta.oml.server.parser.FeatureParser;

/**
 * １度の接続で複数の命令を実行するサービス.
 */
public abstract class BatchService implements Runnable {
  Socket socket;
  Learner learner;
  FeatureParser parser;

  public BatchService(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    // modelId取得
    // パーサー名取得
    // whileループでjson読み込み
    // exec(Label label, List features)
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String configJson = in.readLine();
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      if(this.socket != null) {
        try {
          this.socket.close();
        } catch(IOException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    }
  }

  abstract Label exec(Label label, List<Feature> x);
}
