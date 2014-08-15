AndroidLogCollector
===================
###android app崩溃日志收集sdk 1.0

作者：贾博士

##崩溃日志收集方法：

1.LogCollector是lib包，在需要添加崩溃日志sdk的工程中导入此包。

2.导入lib后，在自己的工程的AndroidManifest.xml文件中加入权限：

			<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
			<uses-permission android:name="android.permission.INTERNET"/>
			<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
			<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
			<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />


3.在自己的工程中重写自己的application，在oncreate中加入

		LogCollector.init(getApplicationContext(), UPLOAD_URL, params);
		
PS:重写自己的application记得在Manifest注册

>参数：
>
>>1.Context
>
>>2.String UPLOAD_URL ，上传地址url，支持http和https方式，post方法。
>
>>3.HttpParameters params ，自定义的类，用于post上传其他参数。



>
>说明：
>
>>发生崩溃后，日志会保存在 /data/data/{your package name}/files/  目录下
>
>>日志只有一个文件，多条数据日志保存
>
>>日志文件上传成功后会自动删除，上传不成功则不删除
>
>>日志格式可以根据自己的需求在源码中改动
>
>>http的post传的数据也可自行修改
>

参考代码：

		public class MyApplication extends Application {
			//post method , upload logfile url,replace your site . support http or https
			private static final String UPLOAD_URL = "http://xxxxxxxx";
		
			@Override
			public void onCreate() {
			super.onCreate();

			//upload logfile , post params.
			HttpParameters params = new HttpParameters();
			params.add("key1", "value1");
			params.add("key2", "value2");
			params.add("key3", "value3");
			//.......
			//replace your key and value;

			boolean isDebug = true;
			//set debug mode , you can see debug log , and also you can get logfile in sdcard;
			LogCollector.setDebugMode(isDebug);
			LogCollector.init(getApplicationContext(), UPLOAD_URL, params);//params can be null
			}
		}
		
##上传日志方法：

需要先在application中执行init，

然后在任何位置添加如下代码：

		LogCollector.upload(boolean isWifiOnly);
 

>参数：
>
>>1.boolean isWifiOnly; true代表只在wifi情况下发送，false代表有网的情况下就发送（包括流量和wifi）

您可以在service，activity等位置的合适时机触发，不会卡界面也不会影响性能。

参考代码：

		private void uploadLogFile(){
		boolean isWifiOnly = true;//only wifi mode can upload
		LogCollector.upload(isWifiOnly);//upload at the right time
		}

##调试：

在init之前调用

		LogCollector.setDebugMode(boolean isDebug);
		
>说明：
>
>1.开启调试后可以看到堆栈的log信息
>
>2.同时可以在sd卡下的Android/data/{your package name}/Log/ 目录下可以查看保存的崩溃log日志
>
>>该目录下的日志不会自动删除，请及时清空，避免占用空间
>