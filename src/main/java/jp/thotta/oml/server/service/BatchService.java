package jp.thotta.oml.server.service;

import java.util.List;
import java.lang.Runnable;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.Gson;
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
    try {
      Gson gson = new Gson();
      BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      String configJson = in.readLine();
      InputConfig config = gson.fromJson(configJson, InputConfig.class);
      this.learner = config.getLearner();
      this.parser = config.getParser();
      String inJson;
      while((inJson = in.readLine()) != null) {
        IOData inData = gson.fromJson(inJson, IOData.class);
        List<Feature> x = this.parser.parse(inData.features);
        Label inLabel = this.learner.createLabelInstance();
        inLabel.parse(inData.label);
        Label outLabel = exec(inLabel, x);
        IOData outData = new IOData(outLabel.getText(), null);
        String outJson = gson.toJson(outData);
        out.println(outJson);
      }
      this.finalizeService();
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
  abstract void finalizeService();
}
