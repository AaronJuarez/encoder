package com.leantass.encoder;

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
  private ParamEncoderImpl instance;

  @Mock ParamEncoderObject paramEncoderObject;
  @Mock ParamEncoderArray paramEncoderArray;
}
