package com.leantass.encoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.leantass.encoder.ParamEncoderArray.ElementEntry;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for class {@link ParamEncoderArray.ElementEntry}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ElementEntryTest {

  private static final String PARAM_1 = "param1";
  private static final String PARAM_2 = "param2";
  private static final String VALUE_1 = "VALUE1";
  private static final String VALUE_2 = "VALUE2";
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldBeEqualSameInstance() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    ElementEntry ee2 = ee1;

    assertEquals(ee1, ee2);
    assertEquals(ee1.hashCode(), ee2.hashCode());
    assertEquals(ee1.getKey(), ee2.getKey());
    assertEquals(ee1.getValue(), ee2.getValue());
  }

  @Test
  public void shouldBeEqualTwoDifferentInstances() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    ElementEntry ee2 = new ElementEntry(PARAM_1, VALUE_1);

    assertEquals(ee1, ee2);
    assertEquals(ee1.hashCode(), ee2.hashCode());
    assertEquals(ee1.getKey(), ee2.getKey());
    assertEquals(ee1.getValue(), ee2.getValue());
  }

  @Test
  public void shouldNotBeEqualToNul() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);

    assertNotEquals(ee1, null);
  }

  @Test
  public void shouldNotBeEqualToObject() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    Object ee2 = new Object();

    assertNotEquals(ee1, ee2);
    assertNotEquals(ee1.hashCode(), ee2.hashCode());
  }

  @Test
  public void shouldNotBeEqualTwoDifferentInstances() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    ElementEntry ee2 = new ElementEntry(PARAM_2, VALUE_2);

    assertNotEquals(ee1, ee2);
    assertNotEquals(ee1.hashCode(), ee2.hashCode());
    assertNotEquals(ee1.getKey(), ee2.getKey());
    assertNotEquals(ee1.getValue(), ee2.getValue());
  }

  @Test
  public void shouldNotBeEqualTwoDifferentInstancesSameKeyDifferentValue() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    ElementEntry ee2 = new ElementEntry(PARAM_1, VALUE_2);

    assertNotEquals(ee1, ee2);
    assertNotEquals(ee1.hashCode(), ee2.hashCode());
    assertEquals(ee1.getKey(), ee2.getKey());
    assertNotEquals(ee1.getValue(), ee2.getValue());
  }

  @Test
  public void shouldNotBeEqualTwoDifferentInstancesSameValueDifferentKey() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);
    ElementEntry ee2 = new ElementEntry(PARAM_2, VALUE_1);

    assertNotEquals(ee1, ee2);
    assertNotEquals(ee1.hashCode(), ee2.hashCode());
    assertNotEquals(ee1.getKey(), ee2.getKey());
    assertEquals(ee1.getValue(), ee2.getValue());
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingKey() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Key is missing.");
    new ElementEntry(null, VALUE_1);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingValue() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Value is missing.");
    new ElementEntry(PARAM_2, null);
  }

  @Test
  public void shouldThrowUnsupportedOperationException() {
    ElementEntry ee1 = new ElementEntry(PARAM_1, VALUE_1);

    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage("Operation is not supported.");
    ee1.setValue("new value");
  }
}
