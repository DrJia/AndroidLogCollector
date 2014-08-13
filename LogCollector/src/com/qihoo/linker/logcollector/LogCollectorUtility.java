package com.qihoo.linker.logcollector;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class LogCollectorUtility {
	
	private static final String TAG = LogCollectorUtility.class.getName();
	
	
	/**
	 * 获取程序外部(sd)的目录
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalDir(Context context , String dirName) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir + "/");
	}
	
	public static boolean isSDcardExsit() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
	
	public static boolean hasPermission(Context context) {
		if (context != null) {
			boolean b1 = context
					.checkCallingOrSelfPermission("android.permission.INTERNET") == 0;// 
			boolean b2 = context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0;
			boolean b3 = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
			boolean b4 = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0;
			boolean b5 = context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0;
			
			if(!b1 || !b2 || !b3 || !b4 || !b5){
				Log.d(TAG, "没有添加权限");
				Toast.makeText(context.getApplicationContext(), "没有添加权限", Toast.LENGTH_SHORT).show();
			}
			return b1 && b2 && b3 && b4 && b5;
		}

		return false;
	}
	
	public static String getCurrentTime(){
		long currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String time = sdf.format(date);
		return time;
	}
	
	public static String getVerName(Context c){
		PackageManager pm = c.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
			e.printStackTrace();
			return "error";
		}
		if(pi == null){
			return "error1";
		}
		String versionName = pi.versionName;
		if(versionName == null){
			return "not set";
		}
		return versionName;
	}
	
	public static String getVerCode(Context c){
		PackageManager pm = c.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
			e.printStackTrace();
			return "error";
		}
		if(pi == null){
			return "error1";
		}
		int versionCode = pi.versionCode;
		
		return String.valueOf(versionCode);
	}

	public static String getMid(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
        String AndroidID = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
        String serialNo = getDeviceSerialForMid2();
        String m2 = getMD5Str("" + imei + AndroidID + serialNo);
        return m2;
    }
	
	private static String getDeviceSerialForMid2() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }
	
	public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        

        return md5StrBuff.toString();
    }
}
