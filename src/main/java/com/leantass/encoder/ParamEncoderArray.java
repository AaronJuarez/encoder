package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Specifies the behavior to encode arrays.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderArray {

  private static final String START = "[";
  private static final String END = "]";
  private static final String DELIMITER = ",";
  private static final String EMPTY = "[]";
  private final ParamEncoderObject objectEncoder = new ParamEncoderObject();

  public String encode(Entry<String, Object> entry, RuleEncoder rule) {
    checkNotNull(entry, "Entry is missing.");
    String resultString = "";
    if (rule != null) {
      resultString = encode(rule, entry);
      if (resultString.equals(EMPTY)) {
        resultString = "";
      }
    }
    return resultString;
  }

  private String encode(RuleEncoder rule, Entry<String, Object> entry) {
    StringBuilder resultString = new StringBuilder(START);
    Object[] array = (Object[]) entry.getValue();
    for (Object object : array) {
      String result = 
          objectEncoder.encode(new DefaultEntry(entry.getKey(), object), rule);
      if (resultString.length() + result.length() + 2 <= rule.getArrayWidth()) {
        if (resultString.length() > 1) {
          resultString.append(DELIMITER);
        }
        resultString.append(result);
      } else {
        break;
      }
    }
    return resultString.append(END).toString();
  }

  private class DefaultEntry implements Entry<String, Object> {

    private final String key;
    private final Object value;

    private DefaultEntry(String key, Object value) {
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

    @Override
    public Object setValue(Object value) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}