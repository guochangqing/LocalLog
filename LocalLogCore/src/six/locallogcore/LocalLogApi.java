package six.locallogcore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalLogApi {
	private static OnClickListener listener;
	private static int num = 0;
	private static long lasttime = 0;
	private static ArrayList<LogEntity> records;
	private static boolean isdebug = true;
	private static boolean iswarn = true;
	private static boolean iserror = true;
	private static long recordtime = 0;
	private static String sdcardpath;
	private static Timer mTimer;
	private static TimerTask mTimerTask;
	private static boolean isCheck = true;
	private static Handler handler;
	public static void initLocalLogEngine(Context context){
		if(null == handler){
			handler = new Handler(Looper.getMainLooper()){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					num = 0;
					lasttime = 0;
				}
			};
		}
		if(null == records){
			records = new ArrayList<LogEntity>();
		}
		isCheck = true;
		num = 0;
		lasttime = 0;
		isdebug = NorUtil.ReadSharedPreferencesBoolean(context,"locallog","isdebug",true);
		iswarn = NorUtil.ReadSharedPreferencesBoolean(context,"locallog","iswarn",true);
		iserror = NorUtil.ReadSharedPreferencesBoolean(context,"locallog","iserror",true);
		//1800000
		recordtime = NorUtil.ReadSharedPreferencesLong(context, "locallog", "recordtime",1800000);
		sdcardpath = getSDPath();
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mTimer = new Timer(true);
		mTimerTask = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (records) {
					if(isCheck){
						long cur = System.currentTimeMillis();
						ArrayList<LogEntity> list = new ArrayList<LogEntity>();
						for(LogEntity temp:records){
							if(cur-temp.getTime()>recordtime){
								list.add(temp);
							}else{
								break;
							}
						}
						if(list.size()>0){
							records.removeAll(list);
						}
					}
				}
			}
		};
		mTimer.schedule(mTimerTask, 1000, 1000);
	}
	public static void destroyLocalLogEngine(){
		isCheck = false;
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if(null != records){
			records.clear();
			records = null;
		}
		listener = null;
		handler = null;
	}
	//error
	public static void recordErrorMsg(String msg){
		String temp = msg;
		if(null == temp){
			temp = "\n";
		}else{
			temp = "【error】-> "+msg;
		}
		if(iserror){
			addLogEntity(new LogEntity(LogEntity.TYPE_ERROR,System.currentTimeMillis(),temp));
		}
	}
	//debug
	public static void recordDebugMsg(String msg){
		String temp = msg;
		if(null == temp){
			temp = "\n";
		}else{
			temp = "【debug】-> "+msg;
		}
		if(isdebug){
			addLogEntity(new LogEntity(LogEntity.TYPE_DEBUG,System.currentTimeMillis(),temp));
		}
	}
	//warn
	public static void recordWarnMsg(String msg){
		String temp = msg;
		if(null == temp){
			temp = "\n";
		}else{
			temp = "【warn】-> "+msg;
		}
		if(iswarn){
			addLogEntity(new LogEntity(LogEntity.TYPE_WARN,System.currentTimeMillis(),temp));
		}
	}
	private static void addLogEntity(LogEntity entity){
		synchronized (records) {
			if(null == entity){
				return;
			}
			records.add(entity);
		}
	}
	

	private static String getSDPath() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			if(null != sdDir){
				return sdDir.toString();
			}
		}
		return null;
	}
	
	//dialog
	private static void showMainDialog(final Context context){
		LinearLayout linear = new LinearLayout(context);
		linear.setOrientation(LinearLayout.VERTICAL);
		Button b1 = new Button(context);
		b1.setText("更改配置");
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showConfigDialog(context);
			}
		});
		linear.addView(b1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Button b2 = new Button(context);
		b2.setText("开始写入");
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(null == sdcardpath){
					Toast.makeText(context, "sdcard不存在", Toast.LENGTH_SHORT).show();
				}else{
					if(null != records && records.size()>0){
						showProgressDialog(context);
					}else{
						Toast.makeText(context, "未发现待写入信息", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		linear.addView(b2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		new AlertDialog.Builder(context).setTitle("本地日志系统")
		.setView(linear).setNegativeButton("取消", null).show();

	}
	private static void showProgressDialog(final Context context){
		new AsyncTask<Void, Void, Boolean>(){
			ProgressDialog mpDialog;
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				synchronized (records) {
					boolean result = true;
					try {
						String path = sdcardpath+File.separator+"locallog";
						File file = new File(path); 
				        if(file.exists()) {  
				            file.delete();
				        } 
				        file.createNewFile();
				        FileOutputStream stream = new FileOutputStream(file); 
				        for(LogEntity temp:records){
				        	byte[] buf = temp.getContent().getBytes();  
				            stream.write(buf);  
				        }
				        stream.close();
					} catch (IOException e) {
						result = false;
					} 
					return result;
				}
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				isCheck = false;
				mpDialog = new ProgressDialog(context); 
				mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mpDialog.setMessage("写入文件中，请稍后"); 
				mpDialog.setCancelable(false);  
				mpDialog.show();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				isCheck = true;
				if(null != mpDialog && mpDialog.isShowing()){
					mpDialog.dismiss();
				}
			}
			
		}.execute();
	}
	private static void showConfigDialog(final Context context){
		ScrollView scroll = new ScrollView(context);
		LinearLayout linear = new LinearLayout(context);
		linear.setOrientation(LinearLayout.VERTICAL);
		scroll.addView(linear, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		final CheckBox box1 = new CheckBox(context);
		box1.setText("是否写入【error】级别信息");
		box1.setChecked(iserror);
		linear.addView(box1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		final CheckBox box2 = new CheckBox(context);
		box2.setText("是否写入【debug】级别信息");
		box2.setChecked(isdebug);
		linear.addView(box2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		final CheckBox box3 = new CheckBox(context);
		box3.setText("是否写入【warn】级别信息");
		box3.setChecked(iswarn);
		linear.addView(box3, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		LinearLayout l2 = new LinearLayout(context);
		l2.setOrientation(LinearLayout.HORIZONTAL);
		TextView t = new TextView(context);
		t.setText("记录最近："+(recordtime/1000/60)+"分钟   修改：");
		l2.addView(t, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		final EditText text = new EditText(context);
		text.setHint("此处更改时间");
		l2.addView(text, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linear.addView(l2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		new AlertDialog.Builder(context).setTitle("更改配置")
		.setView(scroll).setPositiveButton("保存",new Dialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				iserror = box1.isChecked();
				isdebug = box2.isChecked();
				iswarn = box3.isChecked();
				NorUtil.WriteSharedPreferences(context, "locallog", "isdebug", isdebug);
				NorUtil.WriteSharedPreferences(context, "locallog", "iserror", iserror);
				NorUtil.WriteSharedPreferences(context, "locallog", "iswarn", iswarn);
				String temp = text.getText().toString();
				if(null !=temp && temp.trim().length()>0){
					temp = temp.trim();
					long time = Long.parseLong(temp);
					recordtime = time*1000*60;
					NorUtil.WriteSharedPreferences(context, "locallog", "recordtime", recordtime);
				}
			}
		}).setNegativeButton("取消", null).show();
	}
	/**
	 * 
	 * 事件监听器
	 * 
	 * */
	public static OnClickListener getLocalLogListener(){
		if(null == listener){
			listener = new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(num == 0){
						lasttime = System.currentTimeMillis();
						num++;
						handler.removeMessages(0);
						handler.sendEmptyMessageDelayed(0, 2500);
					}else{
						long time = System.currentTimeMillis();
						if(time-lasttime <500){
							num++;
							lasttime = time;
						}else{
							num = 0;
							lasttime = 0;
						}
					}
					if(num>=5){
						//显示
						num = 0;
						lasttime = 0;
						handler.removeMessages(0);
						showMainDialog(v.getContext());
					}
				}
			};
		}
		return listener;
	}
}
