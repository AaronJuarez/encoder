package com.leantass.encoder;

import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class EncoderTest {

  ParamEncoder encoder;

  @Before
  public void setUp() {
    encoder = new ParamEncoderImpl();
  }

  @Test
  public void addRulestest() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_RIGHT, 4);

//		encoder.
  }

  @Test
  public void test() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_RIGHT, 4);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("int1", 100);
        put("int2", 100);
        put("left2", "ABC");
        put("left3", "ABC");
      }
    });
    System.out.println(result);
  }

}
