/*
 * Copyright (C) 2013 Manuel Peinado
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
package com.example.wing.workingsongpa.AppBarHidden;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;


public class FadingActionBarHelper extends com.example.wing.workingsongpa.AppBarHidden.FadingActionBarHelperBase {

    private android.support.v7.app.ActionBar mActionBar;
    private AppCompatActivity activity;

    @SuppressLint("NewApi")
    @Override
    public void initActionBar(Activity activity) {
        mActionBar = ((AppCompatActivity)activity).getSupportActionBar();
        this.activity = (AppCompatActivity)activity;
        super.initActionBar(activity);
    }



    @SuppressLint("NewApi")
    @Override
    protected int getActionBarHeight() {

//        Resources r = this.activity.getResources();
//        int height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 53, r.getDisplayMetrics());
        int height = mActionBar.getHeight();

        return height;

    }

    @Override
    protected boolean isActionBarNull() {
        return mActionBar == null;
    }

    @SuppressLint("NewApi")
    @Override
    protected void setActionBarBackgroundDrawable(Drawable drawable) {

        mActionBar.setBackgroundDrawable(drawable);
    }
}
