package six.locallogcore;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
@SuppressLint("WorldReadableFiles")
@SuppressWarnings("deprecation")
public class NorUtil {
	
	/**
	 * 获取软件版本
	 * @param context
	 * @return
	 */
	public static String getSoftVersion(Context context){
		String v = null;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			v = info.versionName;   //版本名
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return v;
	}
	/***************************************************************************
	 * 在SharedPreferences中读取指定的内容
	 */
	
	public static Map<String, ?> ReadSharedPreferences(Context context,String name) {
		SharedPreferences userInfo;
		try {
			userInfo = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
			return userInfo.getAll();
		} catch(NullPointerException e) {
			return null;
		}
	}

	/***************************************************************************
	 * 在SharedPreferences中读取指定的内容
	 */
	public static int ReadSharedPreferencesInt(Context context,String name, String key,int defaultvalue) {
		try {
			SharedPreferences userInfo = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
			return userInfo.getInt(key, defaultvalue);
		} catch(NullPointerException e) {
			return -1;
		}
	}

	/***************************************************************************
	 * 在SharedPreferences中读取指定的内容
	 */
	public static boolean ReadSharedPreferencesBoolean(Context context,String name, String key,boolean defaultvalue) {
		try {
			SharedPreferences userInfo = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
			return userInfo.getBoolean(key, defaultvalue);
		} catch(NullPointerException e) {
			return true;
		}
	}

	/***************************************************************************
	 * 在SharedPreferences中读取指定的内容
	 */
	public static String ReadSharedPreferencesString(Context context,String name, String key,String defaultvalue) {
		try {
			SharedPreferences userInfo = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
			return userInfo.getString(key, defaultvalue);
		} catch(NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static long ReadSharedPreferencesLong(Context context,String name, String key,long defaultvalue) {
		try {
			SharedPreferences userInfo = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE);
			return userInfo.getLong(key, defaultvalue);
		} catch(NullPointerException e) {
			e.printStackTrace();
			return 0;
		}
	}
	/***************************************************************************
	 * 向SharedPreferences中写入指定的内容
	 */
	public static void WriteSharedPreferences(Context context,String name, String key, String value) {
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.putString(key, value);
		userInfoEditor.commit();
	}
	/**上唇*/
	public static void clearNativeUninstallList(Context context,String name){
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.clear();
		userInfoEditor.commit();
	}

	/***************************************************************************
	 * 向SharedPreferences中写入指定的内容
	 */
	public static void WriteSharedPreferences(Context context,String name, String key, int value) {
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.putInt(key, value);
		userInfoEditor.commit();
	}
	public static void WriteSharedPreferences(Context context,String name, String key, long value) {
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.putLong(key, value);
		userInfoEditor.commit();
	}
	/***************************************************************************
	 * 向SharedPreferences中写入指定的内容
	 */
	public static void WriteSharedPreferences(Context context,String name, String key, boolean value) {
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.putBoolean(key, value);
		userInfoEditor.commit();
	}
	/**
	 * 清除
	 * @param context
	 * @param name
	 */
	public static void clearSharedPreferences(Context context,String name){
		SharedPreferences.Editor userInfoEditor = context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).edit();
		userInfoEditor.clear();
		userInfoEditor.remove(name);
		userInfoEditor.commit();
	}
}

