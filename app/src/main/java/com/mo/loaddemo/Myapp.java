package com.mo.loaddemo;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Mo on 2017/9/12.
 */

public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
