package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

public class ParamEncoderImpl implements ParamEncoder {

  private static final String AND = "&";
  private static final String EQUAL = "=";
  private final Map<String, RuleEncoder> rules = new HashMap<>();
  private final ParamEncoderObject objectEncoder = new ParamEncoderObject();
  private final ParamEncoderArray arrayEncoder = new ParamEncoderArray(objectEncoder);

  @Override
  public void addFieldTruncationRule(String fieldName, TruncationStyle style,
                                     int maxWidth) {
    RuleEncoder newRule = RuleEncoder.Builder.builder(style).width(maxWidth).build();
    rules.put(fieldName, newRule);
  }

  @Override
  public void addArrayTruncationRule(String fieldName, int maxArrayWidth,
                                     TruncationStyle elemStyle, int maxElemWidth) {
    RuleEncoder newRule = RuleEncoder.Builder.builder(elemStyle).width(maxElemWidth)
        .arrayWidth(maxArrayWidth).build();
    rules.put(fieldName, newRule);
  }

  @Override
  public String encode(SortedMap<String, Object> data) {
    checkNotNull(data, "SortedMap is missing.");
    StringBuilder resultString = new StringBuilder();
    for (Entry<String, Object> entry : data.entrySet()) {
      String tmp = null;
      if (entry.getValue() instanceof Object[]) {
        tmp = arrayEncoder.encode(entry, rules.get(entry.getKey()));
      } else {
        tmp = objectEncoder.encode(entry, rules.get(entry.getKey()));
      }
      if (tmp.length() > 0) {
        if (resultString.length() > 0) {
          resultString.append(AND);
        }
        resultString.append(entry.getKey()).append(EQUAL).append(tmp);
      }
    }
    return resultString.toString();
  }
}