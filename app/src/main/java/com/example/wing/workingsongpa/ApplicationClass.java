package com.example.wing.workingsongpa;

import com.tsengvn.typekit.Typekit;


/**
 * Created by knightjym on 2017. 2. 6..
 */

public class ApplicationClass extends  android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /** 나눔 폰트를 기본으로 설정한다 */
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumGothicBold.ttf"));

    }
}
