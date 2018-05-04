/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wdullaer.materialdatetimepicker.date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.wdullaer.materialdatetimepicker.lunar.Lunar;
import com.wdullaer.materialdatetimepicker.lunar.LunarSolarConverter;

public class SimpleMonthView extends MonthView {

    public SimpleMonthView(Context context, AttributeSet attr, DatePickerController controller) {
        super(context, attr, controller);
    }

    @Override
    public void drawMonthDay(Canvas canvas, int year, int month, int day,
                             int x, int y, int startX, int stopX, int startY, int stopY, int lunarOffsetX, int lunarOffsetY, boolean isShowLunar) {
        if (mSelectedDay == day) {
            canvas.drawCircle(x, y - (MINI_DAY_NUMBER_TEXT_SIZE / 3), DAY_SELECTED_CIRCLE_SIZE,
                    mSelectedCirclePaint);
        }

        if (isHighlighted(year, month, day) && mSelectedDay != day) {
            canvas.drawCircle(x, y + MINI_DAY_NUMBER_TEXT_SIZE - DAY_HIGHLIGHT_CIRCLE_MARGIN,
                    DAY_HIGHLIGHT_CIRCLE_SIZE, mSelectedCirclePaint);
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        } else {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        }

        // gray out the day number if it's outside the range.
        if (mController.isOutOfRange(year, month, day)) {
            mMonthNumPaint.setColor(mDisabledDayTextColor);
        } else if (mSelectedDay == day) {
            mMonthNumPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mMonthNumPaint.setColor(mSelectedDayTextColor);
        } else if (mHasToday && mToday == day) {
            mMonthNumPaint.setColor(mTodayNumberColor);
        } else {
            mMonthNumPaint.setColor(isHighlighted(year, month, day) ? mHighlightedDayTextColor : mDayTextColor);
        }
        String dayString = String.format(mController.getLocale(), "%d", day);

        canvas.drawText(dayString, x, y, mMonthNumPaint);

        if (isShowLunar) {
            Lunar lunarDay = LunarSolarConverter.SolarToLunar(day, mMonth + 1, mYear);

            String lunarDayString = String.valueOf(lunarDay.lunarDay);
            if (lunarDay.lunarDay == 1) {
                lunarDayString += "/" + String.valueOf(lunarDay.lunarMonth);
                mMonthLunarNumPaint.setColor(mDayLunarFirstDayMonthTextColor);
            } else {
                mMonthLunarNumPaint.setColor(mDayLunarTextColor);
            }
            if (mSelectedDay == day) {
                mMonthLunarNumPaint.setColor(mSelectedDayTextColor);
            }

            Rect bounds = new Rect();
            mMonthNumPaint.getTextBounds(dayString, 0, dayString.length(), bounds);
            int height = bounds.height();
            canvas.drawText(lunarDayString, x + lunarOffsetX, y + height + lunarOffsetY, mMonthLunarNumPaint);
        }
    }
}
