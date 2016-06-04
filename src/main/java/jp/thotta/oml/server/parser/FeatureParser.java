package jp.thotta.oml.server.parser;

import jp.thotta.oml.client.io.Feature;

import java.util.List;

/**
 * 特徴ベクトルのパーサインターフェイス.
 */
public interface FeatureParser {
    /**
     * 特徴ベクトルをパースする.
     *
     * @param s 特徴ベクトルの文字列
     */
    public List<Feature> parse(String s);
}
