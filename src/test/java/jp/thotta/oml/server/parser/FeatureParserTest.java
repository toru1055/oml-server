package jp.thotta.oml.server.parser;

import junit.framework.TestCase;
import java.util.List;
import jp.thotta.oml.server.io.Feature;

public class FeatureParserTest extends TestCase {
  public void testFeatureParserBinaryMA() {
    FeatureParserBinaryMA parser = new FeatureParserBinaryMA();
    List<Feature> features = parser.parse( "箸で寿司が食べたいが、箸で寿司は食べられない。");
    for(Feature f : features) {
      //System.out.println(f.key());
    }
    assertEquals(features.size(), 2);
  }

  public void testFeatureParserFactory() {
    FeatureParser p = FeatureParserFactory.createParser("ma");
    assertEquals(p.parse("箸と寿司").size(), 2);
  }
}
