package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map.Entry;

/**
 * Specifies the behavior to encode arrays.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
@Singleton
public class ParamEncoderArray implements Encoder {

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
    this.paramEncoderObject = paramEncoderObject;
  }

  /**
   * Encode the provided array using a given rule.
   *
   * @param entry specifies the array parameter to be encoded
   * @param rule  specifies the rule that will be used to encode the array
   * @return an encoded array as {@link String}
   */
  @Override
  public String encode(Entry<String, Object> entry, @Nullable RuleEncoder rule) {
    checkNotNull(entry, "Entry is missing.");
    String resultString = "";
    if (rule != null) {
      resultString = encodeArray(entry, rule);
    }
    return resultString;
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
    Object[] array = (Object[]) entry.getValue();
    for (Object object : array) {
      String result =
          paramEncoderObject.encode(new ElementEntry(entry.getKey(), object), rule);
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
  private class ElementEntry implements Entry<String, Object> {

    private final String key;
    private final Object value;

    private ElementEntry(String key, Object value) {
      this.key = key;
      this.value = value;
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
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}