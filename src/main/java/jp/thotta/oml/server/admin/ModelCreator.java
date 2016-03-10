package jp.thotta.oml.server.admin;

import jp.thotta.oml.server.ml.*;

/**
 * モデル作成を実行するクラス.
 */
public class ModelCreator {
  public static void main(String[] args) {
    if(args.length != 2) {
      System.err.println("syntax> oml-server create <label-mode>  <learner-type>");
      System.exit(1);
    }
    String labelMode = args[0];
    String learnerType = args[1];
    PathManager.init();
    Learner learner = LearnerFactory.createLearner(labelMode, learnerType);
    System.out.println("Model creation was completed: modelId = " + learner.getModelId());
  }
}
