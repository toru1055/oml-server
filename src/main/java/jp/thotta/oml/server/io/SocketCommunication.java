package jp.thotta.oml.server.io;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.io.InputStreamReader;

/**
 * Server/Client間での通信を管理するクラス.
 */
public class SocketCommunication {
  public static final int TRAIN_BATCH_PORT = 9001;
  public static final int PREDICT_BATCH_PORT = 9002;
  public static final String SERVICE_TRAIN_BATCH = "train_batch";
  public static final String SERVICE_PREDICT_BATCH = "predict_batch";
  static final String SERVICE_NAME_KEY = "service_name";
  static final String STATUS_KEY = "status";
  static final String STATUS_OK = "ok";
  static final String STATUS_NG = "ng";
  static final String MODEL_ID_KEY = "model_id";
  static final String PARSER_TYPE_KEY = "parser_type";
  static final String LABEL_MODE_KEY = "label_mode";
  static final String LABEL_REQUEST_MSG = "label_request";
  static final String LABEL_KEY = "label";
  static final String FEATURES_KEY = "features";
  static final String DELIM = "\t";

  Socket socket;
  BufferedReader in;
  PrintWriter out;

  public SocketCommunication(Socket socket) throws IOException {
    this.socket = socket;
    this.in = new BufferedReader(
        new InputStreamReader(
          socket.getInputStream()));
    this.out = new PrintWriter(
        socket.getOutputStream(), true);
  }

  public void sendStatus(boolean st) {
    String msg = STATUS_KEY + DELIM;
    if(st) {
      msg += STATUS_OK;
    } else {
      msg += STATUS_NG;
    }
    out.println(msg);
  }

  public boolean recvStatus() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return false; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 &&
        STATUS_KEY.equals(kv[0]) &&
        STATUS_OK.equals(kv[1])) {
      return true;
    } else {
      return false;
    }
  }

  public void sendServiceName(String serviceName) {
    String msg = SERVICE_NAME_KEY + DELIM + serviceName;
    out.println(msg);
  }

  public String recvServiceName() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && SERVICE_NAME_KEY.equals(kv[0])) {
      return kv[1];
    } else {
      return null;
    }
  }

  public void sendModelId(int modelId) {
    String msg = MODEL_ID_KEY + DELIM + String.valueOf(modelId);
    out.println(msg);
  }

  public Integer recvModelId() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && MODEL_ID_KEY.equals(kv[0])) {
      return Integer.parseInt(kv[1]);
    } else {
      return null;
    }
  }

  public void sendLabelMode(String labelMode) {
    String msg = LABEL_MODE_KEY + DELIM + labelMode;
    out.println(msg);
  }

  public String recvLabelMode() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && LABEL_MODE_KEY.equals(kv[0])) {
      return kv[1];
    } else {
      return null;
    }
  }

  public void sendParserType(String type) {
    String msg = PARSER_TYPE_KEY + DELIM + type;
    out.println(msg);
  }

  public String recvParserType() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && PARSER_TYPE_KEY.equals(kv[0])) {
      return kv[1];
    } else {
      return null;
    }
  }

  public void sendLabel(String label) {
    String msg = LABEL_KEY + DELIM + label;
    out.println(msg);
  }

  public String recvLabel() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && LABEL_KEY.equals(kv[0])) {
      return kv[1];
    } else {
      return null;
    }
  }

  public void sendFeatures(String features) {
    String msg = FEATURES_KEY + DELIM + features;
    out.println(msg);
  }

  public String recvFeatures() throws IOException {
    String msg = in.readLine();
    if(msg == null) { return null; }
    String[] kv = msg.split(DELIM, 2);
    if(kv.length == 2 && FEATURES_KEY.equals(kv[0])) {
      return kv[1];
    } else {
      return null;
    }
  }
}
