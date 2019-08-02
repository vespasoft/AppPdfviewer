package com.unisys.android.pdfviewer.platform.custom;

import android.animation.ValueAnimator;
import android.view.View;

public class CustomAnimator {

    private static final long DURATION = 2000;

    public enum Property {
        WIDTH,
        HEIGHT,
        TRANSLATIONX,
        TRANSLATIONY
    }

    public static void executeAnimation(View view, float start, float end, Property property) {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(a -> {
            switch (property) {
                case TRANSLATIONX:
                    view.setTranslationX((float) a.getAnimatedValue());
                case TRANSLATIONY:
                    view.setTranslationY((float) a.getAnimatedValue());
            }
            view.requestLayout();
        });
        animator.setDuration(DURATION);
        animator.start();
    }

    /*public static void executeAnimation(View view, int start, int end, Property property) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(a -> {
            switch (property) {
                case WIDTH:
                    view.getLayoutParams().width = (int) a.getAnimatedValue();
                case HEIGHT:
                    view.getLayoutParams().height = (int) a.getAnimatedValue();
            }
            view.requestLayout();
        });
        animator.setDuration(DURATION);
        animator.start();
    }*/

}
