package com.qihoo.linker.logcollector;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;

public class LogFileStorage {

	private static final String TAG = LogFileStorage.class.getName();

	public static final String LOG_SUFFIX = ".log";

	private static final String CHARSET = "UTF-8";

	private static LogFileStorage sInstance;

	private Context mContext;

	private LogFileStorage(Context ctx) {
		mContext = ctx.getApplicationContext();
	}

	public static synchronized LogFileStorage getInstance(Context ctx) {
		if (ctx == null) {
			Log.e(TAG, "Context is null");
			return null;
		}
		if (sInstance == null) {
			sInstance = new LogFileStorage(ctx);
		}
		return sInstance;
	}

	public boolean saveLogFile2Internal(String logString) {
		try {
			File dir = mContext.getFilesDir();
			if (!dir.exists()) {
				dir.mkdir();
			}
			File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
					+ LOG_SUFFIX);
			FileOutputStream fos = new FileOutputStream(logFile , true);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2Internal failed!");
			return false;
		}
		return true;
	}

	public boolean saveLogFile2SDcard(String logString, boolean isAppend) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			Log.e(TAG, "sdcard not exist");
			return false;
		}
		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdir();
			}
			File logFile = new File(logDir, LogCollectorUtility.getMid(mContext)
					+ LOG_SUFFIX);
			/*if (!isAppend) {
				if (logFile.exists() && !logFile.isFile())
					logFile.delete();
			}*/
			FileOutputStream fos = new FileOutputStream(logFile , isAppend);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}

	private File getExternalLogDir() {
		return LogCollectorUtility.getExternalDir(mContext, "Log");
	}
}
