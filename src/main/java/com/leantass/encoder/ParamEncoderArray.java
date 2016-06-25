package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map.Entry;

public class ParamEncoderArray {

  private static final String EQUAL = "=";
  private static final String LEFT_BRACKET = "[";
  private static final String RIGHT_BRACKET = "]";
  private static final String COMMA = ",";

  public String encode(Entry<String, Object> entry, Rule rule) {
    checkNotNull(entry, "SortedMap is missing.");
    StringBuilder resultString = new StringBuilder();
    if (rule != null) {
      for(int i=0; i < ((String[])entry.getValue()).length; i++){
        encode(resultString, rule, entry);
      }
    }
    return resultString.toString();
  }

  private void encode(StringBuilder resultString, Rule currentRule,
      Entry<String, Object> entry) {
    System.out.println("Array " + entry.getKey());
    resultString.append(entry.getKey());
  }


  private String encodeStringLeft(Entry<String, Object> entry, int width) {
    StringBuilder resultString = new StringBuilder();
    String value = entry.getValue().toString();
    if (value.length() > width) {
      String tmp = concatArray(entry, value.substring(0, width));
      resultString.append(tmp);
    } else {
//      defaultConcat(resultString, entry);
    }
    return resultString.toString();
  }

  private String encodeStringRight(Entry<String, Object> entry, int width) {
    StringBuilder resultString = new StringBuilder();
    String value = entry.getValue().toString();
    if (value.length() > width) {
      String tmp = concatArray(entry, value.substring(value.length() - width));
      resultString.append(tmp);
    } else {
//      defaultConcat(resultString, entry);
    }
    return resultString.toString();
  }


  private String concatArray(Entry<String, Object> entry, String value) {
    return COMMA+value;
  }
  
  private String concatInitialArray(Entry<String, Object> entry, String value) {
    return entry.getKey() + EQUAL + LEFT_BRACKET+value;
  }
  
  private String concatEndArray(Entry<String, Object> entry, String value) {
    return RIGHT_BRACKET;
  }
}
