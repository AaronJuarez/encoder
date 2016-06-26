package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.INTEGER;
import static org.junit.Assert.assertEquals;

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

  private static final RuleEncoder RULE = RuleEncoder.Builder.builder(INTEGER).width(2).build();
  private ParamEncoderObject instance;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    instance = new ParamEncoderObject();
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfThree() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 123);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE);
    assertEquals("99", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfTwo() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 98);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE);
    assertEquals("98", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfOne() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 9);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE);
    assertEquals("9", encoded);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingEntry() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Entry is missing.");
    instance.encode(null, RULE);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionMissingEntry() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 'd');
    Entry<String, Object> entry = map.entrySet().iterator().next();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Encoding is not supported.");
    instance.encode(entry, RULE);
  }
}
