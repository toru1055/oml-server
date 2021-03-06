package jp.thotta.oml.server.ml;

import jp.thotta.oml.client.io.*;

import java.util.List;

/**
 * 学習器全般の親クラス.
 */
public abstract class BaseLearner implements Learner {
    protected Integer modelId;
    protected Integer labelMode;
    protected Integer learnerType;

    public BaseLearner(int modelId) {
        this.modelId = modelId;
    }

    public Label createLabelInstance() {
        return LabelFactory.createLabel(labelMode);
    }

    public int getModelId() {
        return this.modelId;
    }

    abstract public void train(Label label, List<Feature> features);

    abstract public Label predict(List<Feature> features);

    abstract public void save();

    abstract public void read();
}
