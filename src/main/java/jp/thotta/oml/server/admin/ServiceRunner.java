package jp.thotta.oml.server.admin;

import jp.thotta.oml.server.service.*;
import jp.thotta.oml.client.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ソケット接続し、サービス実行スレッドを生成する.
 * 指定されたモードでプロセスを起動する
 */
public class ServiceRunner {
  static final int BATCH_THREAD_NUM = 5;
  public static enum ServiceMode {BATCH, ONLINE};
  public static enum CommandMode {TRAIN, PREDICT};

  public static void runService(ServiceMode serviceMode,
                                CommandMode commandMode) {
    ExecutorService ex = null;
    ServerSocket server = null;
    Socket socket = null;
    if(serviceMode == ServiceMode.BATCH) {
      ex = Executors.newFixedThreadPool(BATCH_THREAD_NUM);
      try {
        if(commandMode == CommandMode.TRAIN) {
          server = new ServerSocket(SocketCommunication.TRAIN_BATCH_PORT);
          while(true) {
            socket = server.accept();
            ex.execute(new TrainBatchService(socket));
          }
        } else if(commandMode == CommandMode.PREDICT) {
          server = new ServerSocket(SocketCommunication.PREDICT_BATCH_PORT);
          while(true) {
            socket = server.accept();
            ex.execute(new PredictBatchService(socket));
          }
        }
      } catch(Exception e) {
        e.printStackTrace();
        System.exit(1);
      } finally {
        ex.shutdownNow();
        if(server != null) {
          try {
            server.close();
          } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
          }
        }
      }
    } else if(serviceMode == ServiceMode.ONLINE) {
    } else {
    }
  }

  public static void main(String[] args) {
    if(args.length < 2) {
      System.err.println("Syntax error.");
      System.err.println("$ oml-server run batch|online train|predict");
      System.exit(1);
    }
    String serviceModeText = args[0];
    String commandModeText = args[1];
    ServiceMode sm = ServiceMode.BATCH;
    CommandMode cm = CommandMode.TRAIN;
    if("online".equals(serviceModeText)) {
      sm = ServiceMode.ONLINE;
    }
    if("predict".equals(commandModeText)) {
      cm = CommandMode.PREDICT;
    }
    runService(sm, cm);
  }
}
