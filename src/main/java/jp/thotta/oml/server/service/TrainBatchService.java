package jp.thotta.oml.server.service;

import jp.thotta.oml.client.io.*;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * バッチ処理で学習するサービス.
 */
public class TrainBatchService extends BatchService implements Runnable {
    public TrainBatchService(Socket socket) {
        super(socket);
    }

    protected boolean configureServiceName() {
        return true;
    }

    protected void executeService() throws IOException {
        Label label = this.learner.createLabelInstance();
        while (true) {
            String labelText = comm.recvLabel();
            String featuresText = comm.recvFeatures();
            if (labelText != null && featuresText != null) {
                List<Feature> x = this.parser.parse(featuresText);
                label.parse(labelText);
                this.learner.train(label, x);
                comm.sendStatus(true);
            } else {
                break;
            }
        }
    }

    protected void finalizeService() {
        this.learner.save();
    }
}
