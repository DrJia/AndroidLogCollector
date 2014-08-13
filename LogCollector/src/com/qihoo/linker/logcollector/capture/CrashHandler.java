package com.qihoo.linker.logcollector.capture;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.qihoo.linker.logcollector.utils.LogCollectorUtility;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler {
	
	private static final String TAG = CrashHandler.class.getName();
	
	private static CrashHandler sInstance;
	
	private Context mContext;
	
	private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
	
	String appVerName;
	
	String appVerCode;
	
	String OsVer;
	
	String vendor;
	
	String model;
	
	String mid;

	private CrashHandler(Context c) {
		mContext = c.getApplicationContext();
		//mContext = c;
		appVerName = "appVerName:" + LogCollectorUtility.getVerName(mContext);
		appVerCode = "appVerCode:" + LogCollectorUtility.getVerCode(mContext);
		OsVer = "OsVer:" + Build.VERSION.RELEASE;
		vendor = "vendor:" + Build.MANUFACTURER;
		model = "model:" + Build.MODEL;
		mid = "mid:" + LogCollectorUtility.getMid(mContext);
	}
	
	public static CrashHandler getInstance(Context c){
		if(c == null){
			Log.e(TAG, "Context is null");
			return null;
		}
		if(sInstance == null){
			sInstance = new CrashHandler(c);
		}
		return sInstance;
	}
	
	public void init(){
		boolean b = LogCollectorUtility.hasPermission(mContext);
		if(!b){
			return;
		}
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		//
		handleException(ex);
		//
		ex.printStackTrace();
		
		if(mDefaultCrashHandler != null){
			mDefaultCrashHandler.uncaughtException(thread, ex);
		}else{
			Process.killProcess(Process.myPid());
			//System.exit(1);
		}
	}
	
	private void handleException(Throwable ex){
		String s = fomatCrashInfo(ex);
		Log.d(TAG, s);
		LogFileStorage.getInstance(mContext).saveLogFile2Internal(s);
		LogFileStorage.getInstance(mContext).saveLogFile2SDcard(s, true);
	}
	
	private String fomatCrashInfo(Throwable ex){
		
		String lineSeparator = System.getProperty("line.separator");
		if(TextUtils.isEmpty(lineSeparator)){
			lineSeparator = "\n";
		}
		
		StringBuilder sb = new StringBuilder();
		String logTime = "logTime:" + LogCollectorUtility.getCurrentTime();
		
		String exception = "exception:" + ex.getCause();
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);
		String crashDump = "crashDump:" + "{" + info.toString() + "}";
		printWriter.close();
		String crashMD5 = "crashMD5:" + LogCollectorUtility.getMD5Str(crashDump);
		
		sb.append("&start---").append(lineSeparator);
		sb.append(logTime).append(lineSeparator);
		sb.append(appVerName).append(lineSeparator);
		sb.append(appVerCode).append(lineSeparator);
		sb.append(OsVer).append(lineSeparator);
		sb.append(vendor).append(lineSeparator);
		sb.append(model).append(lineSeparator);
		sb.append(mid).append(lineSeparator);
		sb.append(exception).append(lineSeparator);
		sb.append(crashMD5).append(lineSeparator);
		sb.append(crashDump).append(lineSeparator);
		sb.append("&end---").append(lineSeparator).append(lineSeparator).append(lineSeparator);
		
		return sb.toString();

	}

}
