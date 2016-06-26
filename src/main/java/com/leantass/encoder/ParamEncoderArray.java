package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map.Entry;
import java.util.Objects;

import com.google.common.annotations.VisibleForTesting;

/**
 * Specifies the behavior to encode arrays. The arrays are encoded as comma-separated values
 * enclosed by square brackets. Each element of the array is to be encoded in the given
 * TruncationStyle, with no element taking more than {@link RuleEncoder#getWidth()} characters.
 * The array as a whole, including the square brackets and the separator commas, should not take
 * more than {@link RuleEncoder#getArrayWidth()} characters. For example, consider an array field
 * "x" with maximum array width ({@link RuleEncoder#getArrayWidth()}) 10 using
 * {@link ParamEncoder.TruncationStyle#STRING_RIGHT} truncation style and maximum element width
 * ({@link RuleEncoder#getWidth()}) 3:
 * <ul>
 *   <li>A value of ”AB”, ”CD” will be encoded as [AB,CD]</li>
 *   <li>A value of ”AB”, ”CDEF” will be encoded as [AB,CDE]; the second element got truncated
 *   in STRING_RIGHT style.</li>
 *   <li>A value of ”AB”, ”CDEF”, ”GHIJ”, ”K” will be encoded as [AB,CDE]; including the
 *   encoding of third element will make the encoded string longer than 10 characters, so it and
 *   all subsequent elements are dropped from the output</li>
 * </ul>
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderArray {

  private static final String START = "[";
  private static final String END = "]";
  private static final String DELIMITER = ",";
  private static final String EMPTY_ENCODING = "[]";
  private final ParamEncoderObject paramEncoderObject;

  /**
   * Create a new instance of {@code ParamEncoderArray}.
   *
   * @param paramEncoderObject specifies the object encoder that will be used to encode each
   *                           element in the array
   */
  @Inject
  public ParamEncoderArray(ParamEncoderObject paramEncoderObject) {
    this.paramEncoderObject = checkNotNull(paramEncoderObject, "ParamEncoderObject is missing.");
  }

  /**
   * Encode the provided array using a given rule. For example, consider an array field
   * "x" with maximum array width ({@link RuleEncoder#getArrayWidth()}) 10 using
   * {@link ParamEncoder.TruncationStyle#STRING_RIGHT} truncation style and maximum element width
   * ({@link RuleEncoder#getWidth()}) 3:
   * <ul>
   *   <li>A value of ”AB”, ”CD” will be encoded as [AB,CD]</li>
   *   <li>A value of ”AB”, ”CDEF” will be encoded as [AB,CDE]; the second element got truncated
   *   in STRING_RIGHT style.</li>
   *   <li>A value of ”AB”, ”CDEF”, ”GHIJ”, ”K” will be encoded as [AB,CDE]; including the
   *   encoding of third element will make the encoded string longer than 10 characters, so it and
   *   all subsequent elements are dropped from the output</li>
   * </ul>
   *
   * @param entry specifies the array parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the array
   * @return an encoded array as {@link String}
   */
  public String encode(Entry<String, Object> entry, @Nullable RuleEncoder rule) {
    checkNotNull(entry, "Entry is missing.");
    checkArgument(isEncodingSupported(entry), "Encoding is not supported.");
    String resultString = "";
    if (rule != null) {
      resultString = encodeArray(entry, rule);
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
    return entry.getValue() instanceof String[];
  }

  /**
   * Encode the provided array using a given rule.
   *
   * @param entry specifies the array parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the array
   * @return an encoded array as {@link String}
   */
  private String encodeArray(Entry<String, Object> entry, RuleEncoder rule) {
    checkNotNull(rule, "Rule is missing.");
    StringBuilder resultString = new StringBuilder(START);
    String[] array = (String[]) entry.getValue();
    for (String string : array) {
      String result =
          paramEncoderObject.encode(new ElementEntry(entry.getKey(), string), rule);
      if (resultString.length() + result.length() + 2 <= rule.getArrayWidth()) {
        if (resultString.length() > 1) {
          resultString.append(DELIMITER);
        }
        resultString.append(result);
      } else {
        break;
      }
    }
    String encodedString = resultString.append(END).toString();
    if (encodedString.equals(EMPTY_ENCODING)) {
      encodedString = "";
    }
    return encodedString;
  }

  /**
   * Specifies an array element as an {@link Entry} to encode it.
   *
   * @author jovanimtzrico@gmail.com (Jovani Rico)
   */
  static class ElementEntry implements Entry<String, Object> {

    private final String key;
    private final Object value;

    @VisibleForTesting
    ElementEntry(String key, Object value) {
      this.key = checkNotNull(key, "Key is missing.");
      this.value = checkNotNull(value, "Value is missing.");
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public Object getValue() {
      return value;
    }

    /**
     * This method is not supported as a result if invoked it will throw a
     * {@link UnsupportedOperationException}.
     */
    @Override
    public Object setValue(Object value) {
      throw new UnsupportedOperationException("Operation is not supported.");
    }

    @Override
    public int hashCode() {
      return Objects.hash(key, value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof ElementEntry)) {
        return false;
      }
      ElementEntry tmp = (ElementEntry) obj;
      return Objects.equals(key, tmp.key) && Objects.equals(value, tmp.value);
    }
  }
}