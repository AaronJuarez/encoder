package com.leantass.encoder;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;

import com.google.common.collect.ImmutableSortedMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Integration tests for class {@link ParamEncoderImpl}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderIntegrationTest {

  private ParamEncoder encoder;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    ParamEncoderObject paramEncoderObject = new ParamEncoderObject();
    ParamEncoderArray paramEncoderArray = new ParamEncoderArray(paramEncoderObject);
    encoder = new ParamEncoderImpl(paramEncoderObject, paramEncoderArray);
  }

  @Test
  public void shouldEncodeIntegers() {
    encoder.addFieldTruncationRule("int2", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 2);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "int1", (Object) 10,
            "int2", (Object) 100,
            "int3", (Object) 100,
            "int4", (Object) 10);
    String result = encoder.encode(immutableSortedMap);

    assertEquals("int2=100&int3=99", result);
  }

  @Test
  public void shouldEncodeStrings() {
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addFieldTruncationRule("right3", ParamEncoder.TruncationStyle.STRING_LEFT, 2);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "left1", (Object) "ABC",
            "left2", (Object) "ABCE",
            "right3", (Object) "ABCEF",
            "right1", (Object) "ABC");
    String result = encoder.encode(immutableSortedMap);

    assertEquals("left2=ABC&right3=EF", result);
  }

  @Test
  public void shouldEncodeArrays() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array3", 13, ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    encoder.addArrayTruncationRule("array4", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 2);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "array1", (Object) new String[]{"ABC", "EF", "IJ"},
            "array2", (Object) new String[]{"ABC", "EF", "IJ"},
            "array3", (Object) new String[]{"ABC", "DEFG", "HIJKL"},
            "array4", (Object) new String[]{"ABC", "EF", "IJ"});
    String result = encoder.encode(immutableSortedMap);

    assertEquals("array1=[ABC,EF]&array3=[ABC,EFG,JKL]&array4=[AB,EF,IJ]", result);
  }

  @Test
  public void shouldEncodeIntegerStringsAndArrays() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 4);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "array1", (Object) new String[]{"ABC", "EF", "IJ"},
            "array2", (Object) new String[]{"ABC", "EF", "IJ"},
            "left3", (Object) "ABC",
            "left2", (Object) "ABC",
            "int1", (Object) 100);
    String result = encoder.encode(immutableSortedMap);

    assertEquals("array1=[ABC,EF]&int1=100&left2=ABC&left3=ABC", result);
  }

  @Test
  public void shouldEncodeMixedParameters() {
    encoder.addFieldTruncationRule("left2", ParamEncoder.TruncationStyle.STRING_LEFT, 2);
    encoder.addFieldTruncationRule("left3", ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    encoder.addFieldTruncationRule("right2", ParamEncoder.TruncationStyle.STRING_RIGHT, 2);
    encoder.addFieldTruncationRule("right3", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addFieldTruncationRule("int2", ParamEncoder.TruncationStyle.INTEGER, 2);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 3);
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    ImmutableSortedMap.Builder<String, Object> builder = ImmutableSortedMap.naturalOrder();
    builder.put("array1", (Object) new String[]{"ABC", "EF", "IJ"})
        .put("int2", 100)
        .put("int3", 100)
        .put("left2", "ABC")
        .put("left3", "ABC")
        .put("right2", "ABC")
        .put("right3", "ABC")
        .put("ignored", "1234");
    String res = encoder.encode(builder.build());

    assertEquals("array1=[ABC,EF]&int2=99&int3=100&left2=BC&left3=ABC&right2=AB&right3=ABC", res);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionCannotEncodeObjects() {
    encoder.addFieldTruncationRule("param1", ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    encoder.addFieldTruncationRule("param2", ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "array1", (Object) "ABCDE",
            "array2", (Object) new Object());

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Encoding is not supported.");
    encoder.encode(immutableSortedMap);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionCannotEncodeArrayOfCharacters() {
    encoder.addArrayTruncationRule("array1", 10, ParamEncoder.TruncationStyle.STRING_RIGHT, 3);
    encoder.addArrayTruncationRule("array2", 13, ParamEncoder.TruncationStyle.STRING_LEFT, 3);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "array1", (Object) new Character[]{'A', 'B', 'C'},
            "array2", (Object) new Character[]{'D', 'E', 'F'});

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Encoding is not supported.");
    encoder.encode(immutableSortedMap);
  }
}
