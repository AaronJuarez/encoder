package com.leantass.encoder;

import static org.junit.Assert.*;

import org.junit.Test;

import com.leantass.encoder.Rule.Builder;

public class RuleTest {

  //Negative scenarios for fail fast are missing.
  Rule rule;

  @Test
  public void createRuleObject() {
    Rule.Builder builder = Builder.builder(ParamEncoder.TruncationStyle.INTEGER);
    assertNull(rule);
    rule = builder.width(2).build();
    assertNotNull(rule);
  }

  @Test
  public void ruleAttributes() {
    Rule.Builder builder = Builder.builder(ParamEncoder.TruncationStyle.INTEGER);
    rule = builder.width(2).build();
    assertNotNull(rule);
    assertEquals(rule.getWidth(), 2);
    assertEquals(rule.getStyle(), ParamEncoder.TruncationStyle.INTEGER);
  }
}
