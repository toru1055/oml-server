package jp.thotta.oml.server.parser;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import jp.thotta.oml.server.io.Feature;

/**
 * テキストを形態素解析して特徴ベクトルに変換するパーサ.
 * 0/1のカテゴリカルな素性に変換する
 */
public class FeatureParserBinaryMA implements FeatureParser {
  public List<Feature> parse(String s) {
    ArrayList<Feature> features = new ArrayList<Feature>();
    HashMap<String, Integer> m = new HashMap<String, Integer>();
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(s);
    for(Token token : tokens) {
      if("名詞".equals(token.getPartOfSpeechLevel1())
          && !"数".equals(token.getPartOfSpeechLevel2())) {
        String k = token.getSurface();
        if(m.get(k) == null) {
          m.put(k, 1);
        } else {
          m.put(k, m.get(k) + 1);
        }
        //System.out.println(k + "\t" + token.getPartOfSpeechLevel2());
      }
    }
    for(String k : m.keySet()) {
      Feature f = new Feature(k, 1.0);
      features.add(f);
    }
    return features;
  }
}
