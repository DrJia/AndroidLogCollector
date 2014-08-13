package com.qihoo.linker.logcollector.upload;

import java.io.File;
import java.io.IOException;

import com.qihoo.linker.logcollector.capture.LogFileStorage;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class UploadLogManager {
	
	private static final String TAG = UploadLogManager.class.getName();
	
	private static UploadLogManager sInstance;
	
	private Context mContext;
	
	private HandlerThread mHandlerThread;
	
    private static volatile MyHandler mHandler;
	
    private volatile Looper mLooper;
    
    private volatile boolean isRunning = false;
    
    private String url;
    
    private HttpParameters params;
	
	private UploadLogManager(Context c){
		mContext = c.getApplicationContext();
		mHandlerThread = new HandlerThread(TAG + ":HandlerThread");
		mHandlerThread.start();
		
		
	}

	public static synchronized UploadLogManager getInstance(Context c){
		if(sInstance == null){
			sInstance = new UploadLogManager(c);
		}
		return sInstance;
	}
	
	public void uploadLogFile(String url , HttpParameters params){
		this.url = url;
		this.params = params;
		
		mLooper = mHandlerThread.getLooper();
		mHandler = new MyHandler(mLooper);
		if(mHandlerThread == null){
			return;
		}
		if(isRunning){
			return;
		}
		mHandler.sendMessage(mHandler.obtainMessage());
		isRunning = true;
	}
	
	private final class MyHandler extends Handler{

		public MyHandler(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			File logFile = LogFileStorage.getInstance(mContext).getUploadLogFile();
			if(logFile == null){
				isRunning = false;
				return;
			}
			try {
				String result = HttpManager.uploadFile(url, params, logFile);
				if(result != null){
					LogFileStorage.getInstance(mContext).deleteUploadLogFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				isRunning = false;
			}
		}
		
	}
	
}
