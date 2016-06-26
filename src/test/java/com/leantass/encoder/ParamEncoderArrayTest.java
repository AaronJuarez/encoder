package com.leantass.encoder;

import static com.leantass.encoder.ParamEncoder.TruncationStyle.STRING_LEFT;

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
  public void should() {

  }
}
