package com.leantass.encoder;

import java.util.SortedMap;

/**
 * Specifies the behavior of a parameters encoder.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public interface ParamEncoder {

  /**
   * Specifies the truncation style of the encoding
   *
   * @author jovanimtzrico@gmail.com (Jovani Rico)
   */
  enum TruncationStyle {

    /**
     * Specifies the largest positive number that can be encoded
     */
    INTEGER,
    /**
     * Specifies that the truncating should occurred on the left
     */
    STRING_LEFT,
    /**
     * Specifies that the truncating should occurred on the right
     */
    STRING_RIGHT
  }

  /**
   * Specifies the truncation rules for a given field.
   *
   * @param fieldName specifies the name of the field in which the rule will be
   *                  applied
   * @param style     specifies the truncation style that will be applied
   * @param maxWidth  specifies the maximum width for the rule
   */
  void addFieldTruncationRule(String fieldName, TruncationStyle style,
                              int maxWidth);

  /**
   * Specifies the truncation rules for a an array of given fields.
   *
   * @param fieldName     specifies the name of the field in which the rule will be
   *                      applied
   * @param maxArrayWidth specifies the maximum length of the encoded value
   *                      including the square brackets and the separator commas
   * @param elemStyle     specifies the truncation style that will be applied
   * @param maxElemWidth  specifies the maximum width for the rule
   */
  void addArrayTruncationRule(String fieldName, int maxArrayWidth,
                              TruncationStyle elemStyle, int maxElemWidth);

  /**
   * Performs the encoding of a collection of {@link SortedMap} fields.
   *
   * @param data the collections of parameters that will be encoded
   * @return the equivalent encoded {@link String}
   */
  String encode(SortedMap<String, Object> data);
}
