package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;

/**
 * Specifies the behavior to encode objects.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderObject {

  public String encode(Entry<String, Object> entry, Rule rule) {
    checkNotNull(entry, "SortedMap is missing.");
    String resultString = "";
    if (rule != null) {
      resultString = encode(rule, entry);
    }
    return resultString;
  }

  private String encode(Rule rule, Entry<String, Object> entry) {
    String encoded = null;
    switch (rule.getStyle()) {
      case INTEGER:
        encoded = encodeInteger(entry, rule.getWidth());
        break;
      case STRING_LEFT:
        encoded = encodeStringLeft(entry, rule.getWidth());
        break;
      case STRING_RIGHT:
        encoded = encodeStringRight(entry, rule.getWidth());
        break;
      default:
        throw new UnsupportedOperationException("Operation not supported.");
    }
    return encoded;
  }

  private String encodeInteger(Entry<String, Object> entry, int width) {
    String resultString;
    String value = entry.getValue().toString();
    if (isBeyondUpperBound(value, width)) {
      resultString = getLargestUpperBound(width);
    } else {
      resultString = value;
    }
    return resultString;
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
    String resultString;
    String value = entry.getValue().toString();
    if (value.length() > width) {
      resultString = value.substring(0, width);
    } else {
      resultString = value;
    }
    return resultString;
  }

  private String encodeStringRight(Entry<String, Object> entry, int width) {
    String resultString;
    String value = entry.getValue().toString();
    if (value.length() > width) {
      resultString = value.substring(value.length() - width);
    } else {
      resultString = value;
    }
    return resultString;
  }
}
