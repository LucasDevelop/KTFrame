
package com.lucas.frame.widget.materialprogressbar;

/**
 * A {@code Drawable} that has an intrinsic padding.
 */
public interface IntrinsicPaddingDrawable {

    /**
     * Get whether this drawable is using an intrinsic padding. The default is {@code true}.
     *
     * @return Whether this drawable is using an intrinsic padding.
     */
    boolean getUseIntrinsicPadding();

    /**
     * Set whether this drawable should use an intrinsic padding. The default is {@code true}.
     *
     * @param useIntrinsicPadding Whether this drawable should use its intrinsic padding.
     */
    void setUseIntrinsicPadding(boolean useIntrinsicPadding);
}
