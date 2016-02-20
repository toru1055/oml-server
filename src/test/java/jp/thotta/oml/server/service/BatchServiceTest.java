package jp.thotta.oml.server.service;

import junit.framework.TestCase;
import com.google.gson.Gson;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import jp.thotta.oml.server.io.*;
import jp.thotta.oml.server.admin.*;
import jp.thotta.oml.server.ml.*;

public class BatchServiceTest extends TestCase {
  int modelId;

  protected void setUp() {
    PathManager.setBase("data");
    PathManager.init();
    Learner learner = LearnerFactory.createLearner(
        LabelFactory.BINARY_MODE_TEXT,
        LearnerFactory.SGD_TYPE_TEXT);
    modelId = learner.getModelId();
  }

  public void testTrainBatchService() {
    // Train Server
    Thread serverThread = new Thread(new Runnable() {
      public void run() {
        try {
          ServerSocket server = new ServerSocket(TrainBatchService.PORT);
          Socket socket = server.accept();
          TrainBatchService tbs = new TrainBatchService(socket);
          (new Thread(tbs)).start();
        } catch(Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    });
    serverThread.start();

    // Train Client
    try {
    Socket client = new Socket("localhost", TrainBatchService.PORT);
    Gson gson = new Gson();
    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
    InputConfig config = new InputConfig(modelId, "ma");
    out.println(gson.toJson(config));
    IOData d1 = new IOData("positive", "日エンターと資本業務提携");
    IOData d2 = new IOData("negative", "第2四半期・通期利益予想を下方修正");
    String d1Json = gson.toJson(d1);
    String d2Json = gson.toJson(d2);
    for(int i = 0; i < 10; i++) {
      out.println(d1Json);
      in.readLine();
      out.println(d2Json);
      in.readLine();
    }
    client.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    // Predict Server
    // Predict Client
  }

  protected void tearDown() {
    LearnerFactory.deleteLearner(modelId);
  }
}
