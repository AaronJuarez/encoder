package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;
import java.util.SortedMap;

public class ParamEncoderImpl implements ParamEncoder {

  private final Map<String, Rule> rules = new HashMap<>();
  private static final String AND = "&";

  @Override
  public void addFieldTruncationRule(String fieldName, TruncationStyle style,
      int maxWidth) {
    Rule newRule = Rule.Builder.builder(style).width(maxWidth).build();
    rules.put(fieldName, newRule);
  }

  @Override
  public void addArrayTruncationRule(String fieldName, int maxArrayWidth,
      TruncationStyle elemStyle, int maxElemWidth) {
    Rule newRule = Rule.Builder.builder(elemStyle).width(maxElemWidth).maxArrayWidth(maxArrayWidth).build();
    rules.put(fieldName, newRule);
  }

  @Override
  public String encode(SortedMap<String, Object> data) {
    ParamEncoderObject encoderObject = new ParamEncoderObject();
    ParamEncoderArray encoderArray = new ParamEncoderArray();
    checkNotNull(data, "SortedMap is missing.");
    StringBuilder resultString = new StringBuilder();
    for (Entry<String, Object> entry : data.entrySet()) {
      if (resultString.length() > 0 && hasRule(entry)) {
        resultString.append(AND);
      }
      if (entry.getValue() instanceof String[]) {
        resultString.append(encoderArray.encode(entry, rules.get(entry.getKey())));
      } else {
        resultString.append(encoderObject.encode(entry, rules.get(entry.getKey())));
      }
    }
    return resultString.toString();
  }

  public boolean hasRule(Entry<String, Object> entry) {
    return rules.get(entry.getKey()) != null;
  }
}
