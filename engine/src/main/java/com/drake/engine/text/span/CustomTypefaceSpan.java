/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.engine.text.span;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import androidx.annotation.Nullable;

/**
 * 支持以 Typeface 的方式设置 span 的字体，实现自定义字体的效果
 */
public class CustomTypefaceSpan extends TypefaceSpan {

    /* http://stackoverflow.com/questions/6612316/how-set-spannable-object-font-with-custom-font#answer-10741161 */

    public static final Parcelable.Creator<CustomTypefaceSpan> CREATOR = new Parcelable.Creator<CustomTypefaceSpan>() {
        @Override
        public CustomTypefaceSpan createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public CustomTypefaceSpan[] newArray(int size) {
            return new CustomTypefaceSpan[size];
        }
    };
    private final @Nullable
    Typeface newType;

    /**
     * @param family Typeface 字体的字体名
     * @param type   该字体的 Typeface 对象
     */
    public CustomTypefaceSpan(String family, @Nullable Typeface type) {
        super(family);
        newType = type;
    }

    private static void applyCustomTypeFace(Paint paint, @Nullable Typeface tf) {
        if (tf == null) {
            return;
        }
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(tf);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }
}