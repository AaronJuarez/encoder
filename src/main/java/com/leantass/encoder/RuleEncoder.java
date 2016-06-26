package com.leantass.encoder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.leantass.encoder.ParamEncoder.TruncationStyle;

/**
 * Specifies the encoding rules.
 *
 * @author jovanimtzrico@gmail.com (Jovani Rico)
 */
public final class RuleEncoder {

  private final TruncationStyle style;
  private final int width;
  private final int arrayWidth;

  private RuleEncoder(Builder builder) {
    this.style = builder.style;
    this.width = builder.width;
    this.arrayWidth = builder.arrayWidth;
  }

  public int getWidth() {
    return width;
  }

  public int getArrayWidth() {
    return arrayWidth;
  }

  public TruncationStyle getStyle() {
    return this.style;
  }

  /**
   * Builder of {@link RuleEncoder} instances.
   *
   * @author jovanimtzrico@gmail.com (Jovani Rico)
   */
  public static class Builder {

    private TruncationStyle style;
    private int width;
    private int arrayWidth;

    /**
     * Create a new instance.
     *
     * @param style specifies the {@link TruncationStyle} that will be used.
     */
    private Builder(TruncationStyle style) {
      this.style = checkNotNull(style, "TruncationStyle is missing.");
    }

    /**
     * Specifies the desired width that will be used for the {@link TruncationStyle}.
     *
     * @param width specifies the desired width to be used
     * @return this {@code Builder} object
     * @throws IllegalArgumentException if the specified width is less than zero
     */
    public Builder width(int width) {
      checkArgument(width > 0, "width cannot less than zero.");
      this.width = width;
      return this;
    }

    /**
     * Specifies the desired width of the array that will be used for the {@link TruncationStyle}.
     *
     * @param arrayWidth specifies the desired width of the array to be used
     * @return this {@code Builder} object
     * @throws IllegalArgumentException if the specified width is less than zero
     */
    public Builder arrayWidth(int arrayWidth) {
      checkArgument(arrayWidth > 0, "width cannot be less than zero.");
      this.arrayWidth = arrayWidth;
      return this;
    }

    /**
     * Return a newly-created {@code Rule} based on the contents of the {@code Builder}.
     *
     * @return a new instance with the desired configuration
     */
    public RuleEncoder build() {
      return new RuleEncoder(this);
    }

    /**
     * Returns a new builder with the a specific {@link TruncationStyle}.
     *
     * @param style specifies the desired truncation style that will be used
     * @return a new {@code Builder} instance
     */
    public static final Builder builder(TruncationStyle style) {
      return new Builder(style);
    }
  }
}