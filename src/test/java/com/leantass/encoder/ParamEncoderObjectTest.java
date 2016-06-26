package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.INTEGER;
import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_LEFT;
import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_RIGHT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for class {@link ParamEncoderObject}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderObjectTest {

  private static final RuleEncoder RULE_INTEGER = RuleEncoder.Builder.builder(INTEGER).width(2)
      .build();
  private static final RuleEncoder RULE_LEFT = RuleEncoder.Builder.builder(STRING_LEFT).width(2)
      .build();
  private static final RuleEncoder RULE_RIGHT = RuleEncoder.Builder.builder(STRING_RIGHT).width(2)
      .build();
  private static final String PARAM = "param1";
  private ParamEncoderObject instance;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    instance = new ParamEncoderObject();
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfThree() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) 123);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("99", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfTwo() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "98");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("98", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfOne() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) 9);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("9", encoded);
  }

  @Test
  public void shouldEncodeNegativeIntegerWithWidthOfTwoIntegerLenghtOfOne() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) (-2));
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("-2", encoded);
  }

  @Test
  public void shouldEncodeNegativeIntegerWithWidthOfTwoIntegerLenghtOfTwo() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "-99");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("-9", encoded);
  }

  @Test
  public void shouldEncodeNegativeIntegerWithWidthOfTwoIntegerLenghtOfThree() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) (-123));
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_INTEGER);
    assertEquals("-9", encoded);
  }

  @Test
  public void shouldEncodeStringLeftWithWidthOfTwoLenghtOfOne() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "A");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT);
    assertEquals("A", encoded);
  }

  @Test
  public void shouldEncodeStringLeftWithWidthOfTwoLenghtOfTwo() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "AB");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT);
    assertEquals("AB", encoded);
  }

  @Test
  public void shouldEncodeStringLeftWithWidthOfTwoLenghtOfThree() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "ABC");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT);
    assertEquals("BC", encoded);
  }

  @Test
  public void shouldEncodeStringLeftWithWidthOfTwoLenghtOfFour() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "ABCD");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT);
    assertEquals("CD", encoded);
  }

  @Test
  public void shouldEncodeStringRightWithWidthOfTwoLenghtOfOne() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "A");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT);
    assertEquals("A", encoded);
  }

  @Test
  public void shouldEncodeStringRightWithWidthOfTwoLenghtOfTwo() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "AB");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT);
    assertEquals("AB", encoded);
  }

  @Test
  public void shouldEncodeStringRightWithWidthOfTwoLenghtOfThree() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "ABC");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT);
    assertEquals("AB", encoded);
  }

  @Test
  public void shouldEncodeStringRightWithWidthOfTwoLenghtOfFour() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "ABCD");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT);
    assertEquals("AB", encoded);
  }

  @Test
  public void shouldNotEncodeMissingRule() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) "ABCD");
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, null);
    assertEquals("", encoded);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingEntry() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Entry is missing.");
    instance.encode(null, RULE_INTEGER);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionEncodingNotSupported() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 'd');
    Entry<String, Object> entry = map.entrySet().iterator().next();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Encoding is not supported.");
    instance.encode(entry, RULE_INTEGER);
  }
}
