package com.leantass.encoder;

import java.util.TreeMap;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EncoderTest {

  private ParamEncoder encoder;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    encoder = new ParamEncoderImpl();
  }

  @Test
  public void addInteger() {
    encoder.addFieldTruncationRule("int2", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 2);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("int1", 10);
        put("int2", 100);
        put("int3", 100);
        put("int4", 10);
      }
    });
    assertEquals(result, "int2=100&int3=99");
  }

  @Test
  public void testString() {
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addFieldTruncationRule("right3", ParamEncoder.TruncationStyle.STRING_LEFT, 2);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("left1", "ABC");
        put("left2", "ABCE");
        put("right3", "ABCEF");
        put("right1", "ABC");
      }
    });
    assertEquals(result, "left2=ABC&right3=EF");
  }

  @Test
  public void testArray() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array3", 13, ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    encoder.addArrayTruncationRule("array4", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 2);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("array1", new String[]{"ABC", "EF", "IJ"});
        put("array2", new String[]{"ABC", "EF", "IJ"});
        put("array3", new String[]{"ABC", "DEFG", "HIJKL"});
        put("array4", new String[]{"ABC", "EF", "IJ"});
      }
    });
    assertEquals(result, "array1=[ABC,EF]&array3=[ABC,EFG,JKL]&array4=[AB,EF,IJ]");
  }

  @Test
  public void testMixobjects() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("array1", new String[]{"ABC", "EF", "IJ"});
        put("array2", new String[]{"ABC", "EF", "IJ"});
        put("array3", new String[]{"ABC", "EF", "IJ"});
        put("array4", new String[]{"ABC", "EF", "IJ"});
        put("int1", 100);
        put("int2", 100);
        put("left2", "ABC");
        put("left3", "ABC");
      }
    });
    assertEquals(result, "array1=[ABC,EF]&int1=100&left2=ABC&left3=ABC");
  }

  @Test
  public void testAssigmentExample() {
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 2);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    encoder.addFieldTruncationRule("right2", ParamEncoder.TruncationStyle.STRING_RIGHT, 2);
    encoder.addFieldTruncationRule("right3", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addFieldTruncationRule("int2", ParamEncoder.TruncationStyle.INTEGER, 2);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("array1", new String[]{"ABC", "EF", "IJ"});
        put("int2", 100);
        put("int3", 100);
        put("left2", "ABC");
        put("left3", "ABC");
        put("right2", "ABC");
        put("right3", "ABC");
        put("ignored", "1234");
      }
    });
    assertEquals(result, "array1=[ABC,EF]&int2=99&int3=100&left2=BC&left3=ABC&right2=AB&right3=ABC");
  }

  @Test
  public void testCharacterArray() {
    thrown.expect(IllegalArgumentException.class);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array2", 13, ParamEncoder.TruncationStyle.STRING_LEFT, 3);

    String result = encoder.encode(new TreeMap<String, Object>() {
      {
        put("array1", new Character[]{'A', 'B', 'C'});
        put("array2", new Character[]{'D', 'E', 'F'});
      }
    });
    assertEquals(result, "array1=[A,B,C]&array2=[D,E,F]");
  }
}
