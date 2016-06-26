package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Map.Entry;

/**
 * Specifies the behavior to encode objects. For example, consider a integer field <i>x</i> with
 * maximum width of 2 using this encoding:
 *
 * <ol>
 *   <li> Integer:
 *     <ul>
 *       <li>A value of 1 will be encoded as 1</li>
 *       <li>A value of 12 will be encoded as 12</li>
 *       <li>A value of 123 will be encoded as 99, since 99 is the largest positive number that
 *       can be encoded with 2 characters</li>
 *       <li>A value of -1 will be encoded as -1</li>
 *       <li>A value of -12 will be encoded as -9, since -9 is the smallest negative number that
 *       can be encoded with 2 characters.</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>left truncation</b>:
 *     <ul>
 *       <li>A value of A will be encoded as A</li>
 *       <li>A value of AB will be encoded as AB</li>
 *       <li>A value of ABC will be encoded as BC, truncating on the left</li>
 *       <li>A value of ABCD will be encoded as CD, truncating on the left</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>right truncation</b>:
 *     <ul>
 *       <li>A value of A will be encoded as A</li>
 *       <li>A value of AB will be encoded as AB</li>
 *       <li>A value of ABC will be encoded as AB, truncating on the right</li>
 *       <li>A value of ABCD will be encoded as AB, truncating on the right</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
@Singleton
public class ParamEncoderObject implements Encoder {

  /**
   * Encode the provided parameter as an {@code Entry<String, Object>} using a specific rule. It
   * encodes a {@link String} or {@link Integer} parameter using:
   *
   * <ul>
   *   <li>Integer width</li>
   *   <li>String <b>left</b> truncation</li>
   *   <li>String <b>right</b> truncation</li>
   * </ul>
   *
   * @param entry specifies the parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the given parameter
   * @return an encoded parameter as {@link String}
   */
  @Override
  public String encode(Entry<String, Object> entry, @Nullable RuleEncoder rule) {
    checkNotNull(entry, "Entry is missing.");
    checkArgument(isEncodingSupported(entry), "Encoding is not supported.");
    String resultString = "";
    if (rule != null) {
      resultString = encodeObject(entry, rule);
    }
    return resultString;
  }

  /**
   * Verifies if a given {@code Entry<String, Object>} can be encoded.
   *
   * @param entry specifies the entry to be evaluated
   * @return <b>true</b> if the given entry can be encoded. Otherwise, will return <b>false</b>.
   */
  private boolean isEncodingSupported(Entry<String, Object> entry) {
    return entry.getValue() instanceof String
        || entry.getValue() instanceof Integer;
  }

  /**
   * Encode the provided object using a given rule.
   *
   * @param entry specifies the object parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the array
   * @return an encoded object as {@link String}
   */
  private String encodeObject(Entry<String, Object> entry, RuleEncoder rule) {
    checkNotNull(rule, "Rule is missing.");
    String encoded = null;
    String value = entry.getValue().toString();
    switch (rule.getStyle()) {
      case INTEGER:
        encoded = encodeInteger(value, rule.getWidth());
        break;
      case STRING_LEFT:
        encoded = encodeStringLeft(value, rule.getWidth());
        break;
      case STRING_RIGHT:
        encoded = encodeStringRight(value, rule.getWidth());
        break;
      default:
        throw new UnsupportedOperationException("Operation not supported.");
    }
    return encoded;
  }

  /**
   * Encode a given parameter that contains an {@link Integer}. For example, consider a
   * integer field <i>x</i> with maximum width of 2 using this encoding:
   * <ul>
   *   <li>A value of 1 will be encoded as 1</li>
   *   <li>A value of 12 will be encoded as 12</li>
   *   <li>A value of 123 will be encoded as 99, since 99 is the largest positive number that can
   *   be encoded with 2 characters</li>
   *   <li>A value of -1 will be encoded as -1</li>
   *   <li>A value of -12 will be encoded as -9, since -9 is the smallest negative number that
   *   can be encoded with 2 characters.</li>
   * </ul>
   *
   * @param value the desired parameter to be encoded
   * @param width the desired upper bound that will be used to encode the parameter
   * @return an encoded representation of the provided parameter
   */
  private String encodeInteger(String value, int width) {
    String resultString;
    if (isBeyondUpperBound(value, width)) {
      resultString = getLargestUpperBound(width);
    } else {
      resultString = value;
    }
    return resultString;
  }

  /**
   * Evaluates if the given parameter if beyond the desired bound.
   *
   * @param value the parameter that will be evaluated
   * @param bound the desired upper limit
   * @return <b>true</b> if the provided parameter exceeds the upper limit. Otherwise, will
   * return <b>false</b>
   */
  private boolean isBeyondUpperBound(String value, int bound) {
    Integer intValue = Integer.valueOf(value);
    System.out.println("intValue "+intValue);
    Double limit = Math.log10(intValue);
    return (limit.intValue() + 1) > bound;
  }

  /**
   * Provides the largest value of a given upper limit. For instance if the bound is two, the
   * largest number that can be represented will be 100 and this method will return (100 - 1)
   * <b>99</b> as the largest value below the bound.
   *
   * @param bound specifies the desired bound
   * @return the largest value that can be represented and is below the bound
   */
  private String getLargestUpperBound(int bound) {
    Double limit = Math.pow(10, bound);
    return limit.intValue() - 1 + "";
  }

  /**
   * Encode a given {@link String} parameter using a left truncation style. For example, consider a
   * string field <i>x</i> with maximum width of 2 using this encoding:
   * <ul>
   *   <li>A value of A will be encoded as A</li>
   *   <li>A value of AB will be encoded as AB</li>
   *   <li>A value of ABC will be encoded as BC, truncating on the left</li>
   *   <li>A value of ABCD will be encoded as CD, truncating on the left</li>
   * </ul>
   *
   * @param value specifies the parameter to be encoded
   * @param width specifies the maximum length of the newly encoded string
   * @return an equivalent encoded {@link String} of the given parameter
   */
  private String encodeStringLeft(String value, int width) {
    String resultString;
    if (value.length() > width) {
      resultString = value.substring(value.length() - width);
    } else {
      resultString = value;
    }
    return resultString;
  }

  /**
   * Encode a given {@link String} parameter using a right truncation style. For example, consider a
   * string field <i>x</i> with maximum width of 2 using this encoding:
   * <ul>
   *   <li>A value of A will be encoded as A</li>
   *   <li>A value of AB will be encoded as AB</li>
   *   <li>A value of ABC will be encoded as AB, truncating on the right</li>
   *   <li>A value of ABCD will be encoded as AB, truncating on the right</li>
   * </ul>
   *
   * @param value specifies the parameter to be encoded
   * @param width specifies the maximum length of the newly encoded string
   * @return an equivalent encoded {@link String} of the given parameter
   */
  private String encodeStringRight(String value, int width) {
    String resultString;
    if (value.length() > width) {
      resultString = value.substring(0, width);
    } else {
      resultString = value;
    }
    return resultString;
  }
}