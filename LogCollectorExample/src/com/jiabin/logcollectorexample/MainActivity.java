package com.jiabin.logcollectorexample;

import com.qihoo.linker.logcollector.upload.HttpParameters;
import com.qihoo.linker.logcollector.upload.UploadLogManager;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String url = "http://p.s.360.cn/";
		HttpParameters params = new HttpParameters();
		UploadLogManager.getInstance(getApplicationContext()).uploadLogFile(url, params);
	}

	
}
