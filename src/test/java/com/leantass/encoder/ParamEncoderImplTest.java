package com.leantass.encoder;

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
  ParamEncoderObject paramEncoderObject;
  @Mock
  ParamEncoderArray paramEncoderArray;
  private ParamEncoderImpl instance;
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

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
