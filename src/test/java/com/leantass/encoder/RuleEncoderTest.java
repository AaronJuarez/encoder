package com.leantass.encoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.leantass.encoder.RuleEncoder.Builder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for class {@link RuleEncoder}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class RuleEncoderTest {

  //Negative scenarios for fail fast are missing.
  private RuleEncoder rule;

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void createRuleObject() {
    Builder builder = Builder.builder(ParamEncoder.TruncationStyle.INTEGER);
    assertNull(rule);
    rule = builder.width(2).build();
    assertNotNull(rule);
  }

  @Test
  public void ruleFieldAttributes() {
    Builder builder = Builder.builder(ParamEncoder.TruncationStyle.INTEGER);
    rule = builder.width(2).build();
    assertNotNull(rule);
    assertEquals(rule.getWidth(), 2);
    assertEquals(rule.getStyle(), ParamEncoder.TruncationStyle.INTEGER);
  }

  @Test
  public void ruleArrayAttributes() {
    Builder builder = Builder.builder(ParamEncoder.TruncationStyle.STRING_LEFT);
    rule = builder.width(2).arrayWidth(10).build();
    assertNotNull(rule);
    assertEquals(rule.getWidth(), 2);
    assertEquals(rule.getArrayWidth(), 10);
    assertEquals(rule.getStyle(), ParamEncoder.TruncationStyle.STRING_LEFT);
  }

  @Test
  public void negativeAttributes() {
    thrown.expect(IllegalArgumentException.class);
    Builder builder = Builder.builder(ParamEncoder.TruncationStyle.INTEGER);
    rule = builder.width(-2).build();
  }
}
