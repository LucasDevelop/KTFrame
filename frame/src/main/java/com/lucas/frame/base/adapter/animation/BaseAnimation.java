package com.lucas.frame.base.adapter.animation;

import android.animation.Animator;
import android.view.View;

public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
