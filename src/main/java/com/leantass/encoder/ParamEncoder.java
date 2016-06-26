package com.leantass.encoder;

import java.util.SortedMap;

/**
 * Specifies the behavior to encode a {@link SortedMap} of parameters. The parameters could be
 * specified as {@link Integer} or {@link String}. For example, consider a field <b>x</b> with
 * maxWidth of 2 using this encoding:
 *
 * <ol>
 *   <li> Integer ({@link TruncationStyle#INTEGER}):
 *     <ul>
 *       <li>A value of 1 will be encoded as <b>x=1</b></li>
 *       <li>A value of 12 will be encoded as <b>x=12</b></li>
 *       <li>A value of 123 will be encoded as <b>x=99</b>, since 99 is the largest positive
 *       number that can be encoded with 2 characters</li>
 *       <li>A value of -1 will be encoded as <b>x=-1</b></li>
 *       <li>A value of -12 will be encoded as <b>x=-9</b>, since -9 is the smallest negative number
 *       that can be encoded with 2 characters.</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>left truncation</b> ({@link TruncationStyle#STRING_LEFT}):
 *     <ul>
 *       <li>A value of A will be encoded as <b>x=A</b></li>
 *       <li>A value of AB will be encoded as <b>x=AB</b></li>
 *       <li>A value of ABC will be encoded as <b>x=BC</b>, truncating on the left</li>
 *       <li>A value of ABCD will be encoded as <b>x=CD</b>, truncating on the left</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>right truncation</b> ({@link TruncationStyle#STRING_RIGHT}):
 *     <ul>
 *       <li>A value of A will be encoded as <b>x=A</b></li>
 *       <li>A value of AB will be encoded as <b>x=AB</b></li>
 *       <li>A value of ABC will be encoded as <b>x=AB</b>, truncating on the right</li>
 *       <li>A value of ABCD will be encoded as <b>x=AB</b>, truncating on the right</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * A parameter can be also be specified as {@code String[]} and it will be encoded as follow. For
 * example, consider an array field <b>x</b> with maxArrayWidth 10 using
 * {@link ParamEncoder.TruncationStyle#STRING_RIGHT} truncation style and maxElemWidth of 3:
 * <ul>
 *   <li>A value of ”AB”, ”CD” will be encoded as <b>x=[AB,CD]</b></li>
 *   <li>A value of ”AB”, ”CDEF” will be encoded as <b>x=[AB,CDE]</b>; the second element got
 *   truncated in STRING_RIGHT style.</li>
 *   <li>A value of ”AB”, ”CDEF”, ”GHIJ”, ”K” will be encoded as <b>x=[AB,CDE]</b>; including the
 *   encoding of third element will make the encoded string longer than 10 characters, so it and
 *   all subsequent elements are dropped from the output</li>
 * </ul>
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
