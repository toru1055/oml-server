package jp.thotta.oml.server.service;

import junit.framework.TestCase;
import java.net.Socket;
import java.net.ServerSocket;
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
    String p_label = "positive";
    String p_feature = "日エンターと資本業務提携";
    String n_label = "negative";
    String n_feature = "第2四半期・通期利益予想を下方修正";

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
      SocketCommunication c_comm = new SocketCommunication(client);
      c_comm.sendModelId(modelId);
      c_comm.sendParserType("ma");
      boolean status = c_comm.recvStatus();
      String labelMode = c_comm.recvLabelMode();
      if(status && "binary".equals(labelMode)) {
        for(int i = 0; i < 10; i++) {
          c_comm.sendLabel(p_label);
          c_comm.sendFeatures(p_feature);
          assertTrue(c_comm.recvStatus());
          c_comm.sendLabel(n_label);
          c_comm.sendFeatures(n_feature);
          assertTrue(c_comm.recvStatus());
        }
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
      SocketCommunication c_comm2 = new SocketCommunication(client2);
      c_comm2.sendModelId(modelId);
      c_comm2.sendParserType("ma");
      boolean status = c_comm2.recvStatus();
      String labelMode = c_comm2.recvLabelMode();
      if("ok".equals(status) && "binary".equals(labelMode)) {
        c_comm2.sendFeatures(p_feature);
        assertEquals(c_comm2.recvLabel(), p_label);
        c_comm2.sendFeatures(p_feature);
        assertEquals(c_comm2.recvLabel(), n_label);
      }
      client2.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected void tearDown() {
    LearnerFactory.deleteLearner(modelId);
  }
}
