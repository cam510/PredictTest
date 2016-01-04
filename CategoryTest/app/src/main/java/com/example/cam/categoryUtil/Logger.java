package com.example.cam.categoryUtil;

import android.util.Log;


/**
 * debug, error, info, verbose, warn
 *
 * @author : Daehee Han(@daniel_booknara)
 * @version : 1.0.0
 *
 */
public class Logger {
    private static String TAG = BuildConfig.APP_NAME;
    private Logger() { }

	// debug
	public static void d(String className, String msg) {
		d(TAG, className, msg);
	}
	
	// debug
	public static void d(String className, String tag, String msg) {
		if(BuildConfig.DEBUG)
			Log.d(tag, className + " : " + msg);
	}

	// error
	public static void e(String className, String msg) {
		e(TAG, className, msg);
	}
	
	// error
	public static void e(String className, String tag, String msg) {
		if(BuildConfig.DEBUG)
			Log.e(tag, className + " : " + msg);
	}
	
	// info
	public static void i(String className, String msg) {
		i(TAG, className, msg);
	}
	
	// info
	public static void i(String className, String tag, String msg) {
		if(BuildConfig.DEBUG)
			Log.i(tag, className + " : " + msg);
	}
	
	// verbose
	public static void v(String className, String msg) {
		v(TAG, className, msg);
	}
	
	// verbose
	public static void v(String className, String tag, String msg) {
		if(BuildConfig.DEBUG)
			Log.v(tag, className + " : " + msg);
	}

	// warn
	public static void w(String className, String msg) {
		w(TAG, className, msg);
	}
	
	// warn
	public static void w(String className, String tag, String msg) {
		if(BuildConfig.DEBUG)
			Log.w(tag, className + " : " + msg);
	}
	
}