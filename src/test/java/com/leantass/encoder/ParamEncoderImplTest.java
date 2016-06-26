package com.leantass.encoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import com.google.common.collect.ImmutableSortedMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for class {@link ParamEncoderImpl}.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
@RunWith(MockitoJUnitRunner.class)
public class ParamEncoderImplTest {

  @Mock
  private ParamEncoderObject paramEncoderObject;
  @Mock
  private ParamEncoderArray paramEncoderArray;
  private ParamEncoderImpl encoder;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    encoder = new ParamEncoderImpl(paramEncoderObject, paramEncoderArray);
  }

  @Test
  public void shouldEncode() {
    encoder.addFieldTruncationRule("int1", ParamEncoder.TruncationStyle.INTEGER, 2);
    encoder.addFieldTruncationRule("int2", ParamEncoder.TruncationStyle.INTEGER, 2);
    encoder.addFieldTruncationRule("int3", ParamEncoder.TruncationStyle.INTEGER, 2);
    SortedMap<String, Object> immutableSortedMap =
        ImmutableSortedMap.of(
            "int1", (Object) 99,
            "int2", (Object) 99,
            "int3", (Object) 99);
    when(paramEncoderObject.encode(any(Entry.class), any(RuleEncoder.class))).thenReturn("99");

    String result = encoder.encode(immutableSortedMap);
    assertEquals("int1=99&int2=99&int3=99", result);
    verifyZeroInteractions(paramEncoderArray);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingParamEncoderObject() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("ParamEncoderObject is missing.");
    new ParamEncoderImpl(null, paramEncoderArray);
  }

  @Test
  public void shouldThrowNullPointerExceptionMissingParamEncoderArray() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("ParamEncoderArray is missing.");
    new ParamEncoderImpl(paramEncoderObject, null);
  }
}
