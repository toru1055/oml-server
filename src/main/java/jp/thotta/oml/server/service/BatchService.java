package jp.thotta.oml.server.service;

import jp.thotta.oml.client.io.*;
import jp.thotta.oml.server.ml.Learner;
import jp.thotta.oml.server.ml.LearnerFactory;
import jp.thotta.oml.server.parser.FeatureParser;
import jp.thotta.oml.server.parser.FeatureParserFactory;

import java.io.IOException;
import java.net.Socket;

/**
 * １度の接続で複数の命令を実行するサービス.
 */
public abstract class BatchService implements Runnable {
    Socket socket;
    Learner learner;
    FeatureParser parser;
    SocketCommunication comm;

    public BatchService(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println(getClass().getSimpleName() + " was started...");
        try {
            comm = new SocketCommunication(socket);
            Integer modelId = comm.recvModelId();
            String parserType = comm.recvParserType();
            String labelModeText = "ERROR";
            boolean configStatus = false;
            if (modelId != null && parserType != null) {
                this.learner = LearnerFactory.readLearner(modelId);
                this.parser = FeatureParserFactory.createParser(parserType);
                if (this.learner != null && this.parser != null) {
                    int labelMode = learner.createLabelInstance().getLabelMode();
                    labelModeText = LabelFactory.convertModeText(labelMode);
                    configStatus = true;
                }
            }
            comm.sendStatus(configStatus);
            comm.sendLabelMode(labelModeText);
            if (configStatus) {
                System.out.println("modelId: " + modelId + ", labelMode: " + labelModeText);
                this.executeService();
                this.finalizeService();
            } else {
                System.out.println("Error at configure.");
            }
            System.out.println(getClass().getSimpleName() + " was end.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    abstract boolean configureServiceName();

    abstract void executeService() throws IOException;

    abstract void finalizeService();
}
