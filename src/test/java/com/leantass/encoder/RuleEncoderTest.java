package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.INTEGER;
import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_LEFT;
import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_RIGHT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

  private RuleEncoder rule;

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldCreateNewRuleWithWidthOnly() {
    rule = Builder.builder(INTEGER).width(2).build();

    assertNotNull(rule);
    assertEquals(INTEGER, rule.getStyle());
    assertEquals(2, rule.getWidth());
    assertEquals(0, rule.getArrayWidth());
  }

  @Test
  public void shouldNotCreateNewRuleWithNegativeWidth() {
    Builder builder = Builder.builder(STRING_RIGHT);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("width cannot be less than zero.");
    builder.width(-2);
  }

  @Test
  public void shouldCreateNewRuleWithWidthAndArrayWidth() {
    rule = Builder.builder(STRING_LEFT).width(2).arrayWidth(10).build();

    assertNotNull(rule);
    assertEquals(2, rule.getWidth());
    assertEquals(10, rule.getArrayWidth());
    assertEquals(STRING_LEFT, rule.getStyle());
  }

  @Test
  public void shouldNotCreateNewRuleWithWidthAndArrayWidth() {
    Builder builder = Builder.builder(INTEGER).width(2);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("array width cannot be less than zero.");
    builder.arrayWidth(-10);
  }
}
