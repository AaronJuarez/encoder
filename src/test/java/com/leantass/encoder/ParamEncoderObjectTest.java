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

  private ParamEncoderObject instace;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    instace = new ParamEncoderObject();
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfThree() {
    RuleEncoder rule = RuleEncoder.Builder.builder(INTEGER).width(2).build();
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 123);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instace.encode(entry, rule);
    assertEquals("99", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfTwo() {
    RuleEncoder rule = RuleEncoder.Builder.builder(INTEGER).width(2).build();
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 98);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instace.encode(entry, rule);
    assertEquals("98", encoded);
  }

  @Test
  public void shouldEncodeIntegerWithWidthOfTwoIntegerLenghtOfOne() {
    RuleEncoder rule = RuleEncoder.Builder.builder(INTEGER).width(2).build();
    Map<String, Object> map = ImmutableMap.of("param1", (Object) 9);
    Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instace.encode(entry, rule);
    assertEquals("9", encoded);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingEntry() {
    RuleEncoder rule = RuleEncoder.Builder.builder(INTEGER).width(2).build();

    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Entry is missing.");
    instace.encode(null, rule);
  }
}
