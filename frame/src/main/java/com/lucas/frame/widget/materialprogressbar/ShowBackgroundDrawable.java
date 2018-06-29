package com.lucas.frame.widget.materialprogressbar;

/**
 * A {@code Drawable} that has a background.
 */
public interface ShowBackgroundDrawable {

    /**
     * Get whether this drawable is showing a background. The default is {@code true}.
     *
     * @return Whether this drawable is showing a background.
     */
    boolean getShowBackground();

    /**
     * Set whether this drawable should show a background. The default is {@code true}.
     *
     * @param show Whether background should be shown.
     */
    void setShowBackground(boolean show);
}
