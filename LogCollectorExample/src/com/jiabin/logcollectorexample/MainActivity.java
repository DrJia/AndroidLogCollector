package com.jiabin.logcollectorexample;

import com.qihoo.linker.logcollector.LogCollector;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn_crash;

	private Button btn_upload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_crash = (Button) findViewById(R.id.button1);
		btn_upload = (Button) findViewById(R.id.button2);
		btn_crash.setOnClickListener(this);
		btn_upload.setOnClickListener(this);

		
	}
	
	private void causeCrash(){
		String s = null;
		s.split("1");
	}
	
	private void uploadLogFile(){
		boolean isWifiOnly = true;//only wifi mode can upload
		LogCollector.upload(isWifiOnly);//upload at the right time
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			
			causeCrash();
			break;
		case R.id.button2:
			
			uploadLogFile();
			break;

		default:
			break;
		}
	}

}
