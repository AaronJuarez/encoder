package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.SortedMap;

public class ParamEncoderImpl implements ParamEncoder {

  @Override
  public void addFieldTruncationRule(String fieldName, TruncationStyle style,
      int maxWidth) {
  }

  @Override
  public void addArrayTruncationRule(String fieldName, int maxArrayWidth,
      TruncationStyle elemStyle, int maxElemWidth) {
  }

  @Override
  public String encode(SortedMap<String, Object> data) {
    checkNotNull(data, "SortedMap is missing.");
    StringBuilder resultString = new StringBuilder();
    for (Entry<String, Object> entry : data.entrySet()) {
      if (entry.getValue() instanceof String[]) {
        //resultString.append(encoderObject.encode(entry));
      } else {
        resultString.append("");
      }
    }
    return resultString.toString();

  }
}
