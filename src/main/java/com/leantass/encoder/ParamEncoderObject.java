package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;
import com.leantass.encoder.ParamEncoder.TruncationStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Specifies the behavior to encode objects.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderObject {

  private static final String EQUAL = "=";

  
   public String encode(Entry<String, Object> entry, Rule rule) {
    checkNotNull(entry, "SortedMap is missing.");
    StringBuilder resultString = new StringBuilder();
    if (rule != null) {
      encode(resultString, rule, entry);
    }
    return resultString.toString();
  }

  private void encode(StringBuilder resultString, Rule currentRule,
      Entry<String, Object> entry) {
    String encoded = null;
    switch (currentRule.getStyle()) {
      case INTEGER:
        encoded = encodeInteger(entry, currentRule.getWidth());
        break;
      case STRING_LEFT:
        encoded = encodeStringLeft(entry, currentRule.getWidth());
        break;
      case STRING_RIGHT:
        encoded = encodeStringRight(entry, currentRule.getWidth());
        break;
      default:
        throw new UnsupportedOperationException("Operation not supported.");
    }
    if (encoded != null && encoded.length() > 0) {
      resultString.append(encoded);
    }
  }

  private String encodeInteger(Entry<String, Object> entry, int width) {
    StringBuilder resultString = new StringBuilder();
    String value = entry.getValue().toString();
    if (isBeyondUpperBound(value, width)) {
      String tmp = concat(entry, getLargestUpperBound(width));
      resultString.append(tmp);
    } else {
      defaultConcat(resultString, entry);
    }
    return resultString.toString();
  }

  private boolean isBeyondUpperBound(String value, int bound) {
    Integer intValue = Integer.valueOf(value);
    Double limit = Math.log10(intValue);
    return (limit.intValue() + 1) > bound;
  }

  private String getLargestUpperBound(int bound) {
    Double limit = Math.pow(10, bound);
    return limit.intValue() - 1 + "";
  }

  private String encodeStringLeft(Entry<String, Object> entry, int width) {
    StringBuilder resultString = new StringBuilder();
    String value = entry.getValue().toString();
    if (value.length() > width) {
      String tmp = concat(entry, value.substring(0, width));
      resultString.append(tmp);
    } else {
      defaultConcat(resultString, entry);
    }
    return resultString.toString();
  }

  private String encodeStringRight(Entry<String, Object> entry, int width) {
    StringBuilder resultString = new StringBuilder();
    String value = entry.getValue().toString();
    if (value.length() > width) {
      String tmp = concat(entry, value.substring(value.length() - width));
      resultString.append(tmp);
    } else {
      defaultConcat(resultString, entry);
    }
    return resultString.toString();
  }

  private void defaultConcat(StringBuilder resultString,
      Entry<String, Object> entry) {
    resultString.append(concat(entry, entry.getValue().toString()));
  }

  private String concat(Entry<String, Object> entry, String value) {
    return entry.getKey() + EQUAL + value;
  }
}
