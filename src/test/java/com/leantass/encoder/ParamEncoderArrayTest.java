package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_LEFT;
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
  private static final RuleEncoder RULE = RuleEncoder.Builder.builder(STRING_LEFT).width(2)
      .arrayWidth(10).build();
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
  public void shouldEncodeWitArrayLenghtOfTenAndhWidthOfTwoStringLenghtOfThree() {
    final String[] array = new String[]{"ADBCDE", "GHIJK", "LMN"};
    when(paramEncoderObject.encode(new ElementEntry(PARAM, array[0]), RULE)).thenReturn("DE");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, array[1]), RULE)).thenReturn("JK");
    when(paramEncoderObject.encode(new ElementEntry(PARAM, array[2]), RULE)).thenReturn("MN");
    Map<String, Object> map = ImmutableMap.of(PARAM, (Object) array);
    Map.Entry<String, Object> entry = map.entrySet().iterator().next();

    String encoded = instance.encode(entry, RULE);
    assertEquals("[DE,JK,MN]", encoded);
  }
}
