package com.leantass.encoder;

import javax.annotation.Nullable;
import java.util.Map.Entry;

/**
 * Specifies the behavior of an encoder.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public interface Encoder {

  /**
   * Encode the provided parameter as an {@code Entry<String, Object>} using a specific rule.
   *
   * @param entry specifies the parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the given parameter
   * @return an encoded parameter as {@link String}
   */
  String encode(Entry<String, Object> entry, @Nullable RuleEncoder rule);
}
