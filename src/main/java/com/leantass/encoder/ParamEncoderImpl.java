package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

/**
 * Specifies the behavior to encode a {@link SortedMap} of parameters. The parameters could be
 * specified as {@link Integer} or {@link String}. For example, consider a field <b>x</b> with
 * maxWidth of 2 using this encoding:
 *
 * <ol>
 *   <li> Integer ({@link TruncationStyle#INTEGER}):
 *     <ul>
 *       <li>A value of 1 will be encoded as <b>x=1</b></li>
 *       <li>A value of 12 will be encoded as <b>x=12</b></li>
 *       <li>A value of 123 will be encoded as <b>x=99</b>, since 99 is the largest positive
 *       number that can be encoded with 2 characters</li>
 *       <li>A value of -1 will be encoded as <b>x=-1</b></li>
 *       <li>A value of -12 will be encoded as <b>x=-9</b>, since -9 is the smallest negative number
 *       that can be encoded with 2 characters.</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>left truncation</b> ({@link TruncationStyle#STRING_LEFT}):
 *     <ul>
 *       <li>A value of A will be encoded as <b>x=A</b></li>
 *       <li>A value of AB will be encoded as <b>x=AB</b></li>
 *       <li>A value of ABC will be encoded as <b>x=BC</b>, truncating on the left</li>
 *       <li>A value of ABCD will be encoded as <b>x=CD</b>, truncating on the left</li>
 *     </ul>
 *   </li>
 *   <li>
 *     String <b>right truncation</b> ({@link TruncationStyle#STRING_RIGHT}):
 *     <ul>
 *       <li>A value of A will be encoded as <b>x=A</b></li>
 *       <li>A value of AB will be encoded as <b>x=AB</b></li>
 *       <li>A value of ABC will be encoded as <b>x=AB</b>, truncating on the right</li>
 *       <li>A value of ABCD will be encoded as <b>x=AB</b>, truncating on the right</li>
 *     </ul>
 *   </li>
 * </ol>
 *
 * A parameter can be also be specified as {@code String[]} and it will be encoded as follow. For
 * example, consider an array field <b>x</b> with maxArrayWidth 10 using
 * {@link ParamEncoder.TruncationStyle#STRING_RIGHT} truncation style and maxElemWidth of 3:
 * <ul>
 *   <li>A value of ”AB”, ”CD” will be encoded as <b>x=[AB,CD]</b></li>
 *   <li>A value of ”AB”, ”CDEF” will be encoded as <b>x=[AB,CDE]</b>; the second element got
 *   truncated in STRING_RIGHT style.</li>
 *   <li>A value of ”AB”, ”CDEF”, ”GHIJ”, ”K” will be encoded as <b>x=[AB,CDE]</b>; including the
 *   encoding of third element will make the encoded string longer than 10 characters, so it and
 *   all subsequent elements are dropped from the output</li>
 * </ul>
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public class ParamEncoderImpl implements ParamEncoder {

  private static final String AND = "&";
  private static final String EQUAL = "=";
  private final Map<String, RuleEncoder> rules = new HashMap<>();
  private final ParamEncoderObject paramEncoderObject;
  private final ParamEncoderArray paramEncoderArray;

  /**
   * Create a new instance of {@code ParamEncoderImpl}.
   *
   * @param paramEncoderObject specifies the encoder that will be used to encode {@link Integer}
   *                           and {@link String}
   * @param paramEncoderArray specifies the encoded that will be used to encode {@code String[]}
   */
  @Inject
  public ParamEncoderImpl(ParamEncoderObject paramEncoderObject,
                          ParamEncoderArray paramEncoderArray) {
    this.paramEncoderObject = checkNotNull(paramEncoderObject, "ParamEncoderObject is missing.");
    this.paramEncoderArray = checkNotNull(paramEncoderArray, "ParamEncoderArray is missing.");
  }

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
        tmp = paramEncoderArray.encode(entry, rules.get(entry.getKey()));
      } else {
        tmp = paramEncoderObject.encode(entry, rules.get(entry.getKey()));
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