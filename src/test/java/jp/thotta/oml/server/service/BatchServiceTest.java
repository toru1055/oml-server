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
    BufferedReader in;
    PrintWriter out;
    Gson gson = new Gson();
    InputConfig config = new InputConfig(modelId, "ma");
    IOData d1 = new IOData("positive", "日エンターと資本業務提携");
    IOData d2 = new IOData("negative", "日エンター第2四半期・通期利益予想を下方修正");
    String d1Json = gson.toJson(d1);
    String d2Json = gson.toJson(d2);

    // Train Server
    Thread serverThread1 = new Thread(new Runnable() {
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
    serverThread1.start();

    // Train Client
    try {
      Socket client = new Socket("localhost", TrainBatchService.PORT);
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      out = new PrintWriter(client.getOutputStream(), true);
      out.println(gson.toJson(config));
      for(int i = 0; i < 10; i++) {
        out.println(d1Json);
        in.readLine();
        System.out.println(d1Json);
        out.println(d2Json);
        in.readLine();
        System.out.println(d2Json);
      }
      client.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    // Predict Server
    Thread serverThread2 = new Thread(new Runnable() {
      public void run() {
        try {
          ServerSocket server = new ServerSocket(PredictBatchService.PORT);
          Socket socket = server.accept();
          PredictBatchService pbs = new PredictBatchService(socket);
          (new Thread(pbs)).start();
        } catch(Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    });
    serverThread2.start();

    // Predict Client
    try {
      Thread.sleep(1000);
      Socket client2 = new Socket("localhost", PredictBatchService.PORT);
      in = new BufferedReader(new InputStreamReader(client2.getInputStream()));
      out = new PrintWriter(client2.getOutputStream(), true);
      out.println(gson.toJson(config));
      out.println(d1Json);
      IOData r1 = gson.fromJson(in.readLine(), IOData.class);
      assertEquals(r1.label, "positive");
      out.println(d2Json);
      IOData r2 = gson.fromJson(in.readLine(), IOData.class);
      assertEquals(r2.label, "negative");
      client2.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected void tearDown() {
    //LearnerFactory.deleteLearner(modelId);
  }
}
