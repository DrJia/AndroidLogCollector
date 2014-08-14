package com.jiabin.logcollectorexample;

import com.qihoo.linker.logcollector.capture.CrashHandler;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());
		crashHandler.init();
	}

	
}
