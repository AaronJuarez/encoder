package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_LEFT;
import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_RIGHT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.leantass.encoder.ParamEncoderArray.ElementEntry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for class {@link ParamEncoderArray}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
@RunWith(MockitoJUnitRunner.class)
public class ParamEncoderArrayTest {

  private static final String PARAM = "param1";
  private static final String[] ARRAY_THREE = new String[]{"ADBCDE", "GHIJK", "LMN"};
  private static final String[] ARRAY_FOUR = new String[]{"ADBCDE", "GHIJK", "LMN", "JK"};
  private static final RuleEncoder RULE_LEFT_TWO = RuleEncoder.Builder.builder(STRING_LEFT).width(2)
      .arrayWidth(10).build();
  private static final RuleEncoder RULE_LEFT_FOUR = RuleEncoder.Builder.builder(STRING_LEFT)
      .width(4).arrayWidth(10).build();
  private static final RuleEncoder RULE_RIGHT_TWO = RuleEncoder.Builder.builder(STRING_RIGHT)
      .width(2).arrayWidth(10).build();
  private static final RuleEncoder RULE_RIGHT_FOUR = RuleEncoder.Builder.builder(STRING_RIGHT)
      .width(4).arrayWidth(10).build();
  @Mock
  private ParamEncoderObject paramEncoderObject;
  private ParamEncoderArray instance;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    instance = new ParamEncoderArray(paramEncoderObject);
  }

  @Test
  public void shouldEncodeArrayOfThreeElementsWithLenghtOfTenAndhWidthOfTwoStringLeft() {
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[0]), RULE_LEFT_TWO))
        .thenReturn("DE");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[1]), RULE_LEFT_TWO))
        .thenReturn("JK");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[2]), RULE_LEFT_TWO))
        .thenReturn("MN");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_THREE);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT_TWO);
    assertEquals("[DE,JK,MN]", encoded);
  }

  @Test
  public void shouldEncodeArrayOfFourElementsWithLenghtOfTenAndhWidthOfTwoStringLeft() {
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_FOUR[0]), RULE_LEFT_TWO))
        .thenReturn("DE");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_FOUR[1]), RULE_LEFT_TWO))
        .thenReturn("JK");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_FOUR[2]), RULE_LEFT_TWO))
        .thenReturn("MN");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_FOUR[3]), RULE_LEFT_TWO))
        .thenReturn("JK");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_FOUR);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT_TWO);
    assertEquals("[DE,JK,MN]", encoded);
  }

  @Test
  public void shouldEncodeArrayOfThreeElementsWithLenghtOfTenAndhWidthOfFourStringLeft() {
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[0]), RULE_LEFT_FOUR))
        .thenReturn("BCDE");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[1]), RULE_LEFT_FOUR))
        .thenReturn("HIJK");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[2]), RULE_LEFT_FOUR))
        .thenReturn("LMN");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_THREE);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_LEFT_FOUR);
    assertEquals("[BCDE]", encoded);
  }

  @Test
  public void shouldEncodeArrayOfThreeElementsWithLenghtOfTenAndhWidthOfTwoStringRight() {
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[0]), RULE_RIGHT_TWO))
        .thenReturn("AD");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[1]), RULE_RIGHT_TWO))
        .thenReturn("GH");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[2]), RULE_RIGHT_TWO))
        .thenReturn("LM");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_THREE);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT_TWO);
    assertEquals("[AD,GH,LM]", encoded);
  }

  @Test
  public void shouldEncodeArrayOfThreeElementsWithLenghtOfTenAndhWidthOfFourStringRight() {
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[0]), RULE_RIGHT_FOUR))
        .thenReturn("ADBC");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[1]), RULE_RIGHT_FOUR))
        .thenReturn("GHIJ");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, ARRAY_THREE[2]), RULE_RIGHT_FOUR))
        .thenReturn("LMN");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_THREE);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT_FOUR);
    assertEquals("[ADBC]", encoded);
  }

  @Test
  public void shouldEncodeAnEmptyArray() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) new String[]{});
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE_RIGHT_TWO);
    assertEquals("", encoded);
  }

  @Test
  public void shouldNotEncodeMissingRule() {
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) ARRAY_THREE);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, null);
    assertEquals("", encoded);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingEntry() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("Entry is missing.");
    instance.encode(null, RULE_RIGHT_TWO);
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionEncodingNotSupported() {
    Map<String, Object> map = ImmutableMap.of("param1", (Object) new Integer[]{12, 3, 45, 6});
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Encoding is not supported.");
    instance.encode(entry, RULE_LEFT_FOUR);
  }
}
