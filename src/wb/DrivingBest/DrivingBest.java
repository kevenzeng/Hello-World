package wb.DrivingBest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import wb.DrivingBest.R;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DrivingBest extends Activity {
	/** Called when the activity is first created. */
	public static final int OPTION_ORDER = 1;
	public static final int OPTION_RDM = 2;
	public static final int OPTION_TEST = 3;
	public static final int OPTION_WRONGEXERCISE = 4;
	public static final int MODE = MODE_PRIVATE;
	public static final String PREFERENCE_NAME = "SaveSetting";
	public static final String CONFIG_AUTOCHECK = "config_autocheck";
	public static final String CONFIG_AUTO2NEXT = "config_auto2next";
	public static final String CONFIG_AUTO2ADDWRONGSET = "config_auto2addwrongset";
	public static final String CONFIG_SOUND = "config_SOUND";
	public static final String CONFIG_CHECKBYRANDOM = "config_checkbyrandom";
	


	private TextView tv = null;
	private Button btn_order = null;
	private Button btn_rdm = null;
	private Button btn_test = null;
	private Button btn_myWAset = null;
	private Button btn_option = null;

	private Button btn_about = null;
	private Button btn_exit = null;

	Dialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		btn_order = (Button) findViewById(R.id.btn_order);
		btn_rdm = (Button) findViewById(R.id.btn_rdm);
		btn_test = (Button) findViewById(R.id.btn_test);
		btn_myWAset = (Button) findViewById(R.id.btn_myWAset);
		btn_option = (Button) findViewById(R.id.btn_option);

		btn_about = (Button) findViewById(R.id.btn_about);
		btn_exit = (Button) findViewById(R.id.btn_exit);

		// tv = (TextView)findViewById(R.id.ed_text);
		// System.out.println("Main");
		// DataHandle.DataHandler(this);
		//!!!!!!!!!!!!/// DataTrans();//！！！！！！！！txt转入数据库
		//!!!!!!!!sharedPreferencesInit();//!!!!!!!!!!!!!!!!!!初始配置
		judgeTheFirstTime2Run();

		btn_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("option", OPTION_ORDER);
				intent.setClass(DrivingBest.this, ExerciseActivity.class);
				startActivity(intent);
			}
		});

		btn_rdm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("option", OPTION_RDM);
				intent.setClass(DrivingBest.this, ExerciseActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		 * 我的错误集
		 */
		btn_myWAset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(DrivingBest.this,WrongSetShowList.class));
			}
		});

		btn_test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(DrivingBest.this,ExamActivity.class));
			}
		});
		/*
		 * 设置 
		 */
		btn_option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(DrivingBest.this,
						OptionActivity.class));
			}
		});
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitdialog();
			}
		});
		
		btn_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aboutdialog();
			}
		});
	}

	

	private void judgeTheFirstTime2Run() {
		// TODO Auto-generated method stub
		DBAdapter dbAdapter;
		Cursor cursor;
		try {
			dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			cursor = dbAdapter.getAllData();
			if(cursor.getCount()==0){
				new AlertDialog.Builder(this)
					.setTitle("注意！")
					.setMessage("初次配置，按确定后请稍等10秒……")
					.setPositiveButton("确定",new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							DataTrans();
						}
					}).create().show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "test1", Toast.LENGTH_LONG).show();
		}
	}


	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitdialog();
		}
		return false;
	}

	protected void exitdialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认退出吗？");

		builder.setTitle("提示");

		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface v, int which) {
						// TODO Auto-generated method stub
						// dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	protected void aboutdialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("DrivingBest By WB");

		builder.setTitle("关于");

		builder.setPositiveButton("确定",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// dialog.dismiss();
						// this.finish();
					}
				});
		builder.create().show();
	}

	
	private void sharedPreferencesInit() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
		try {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(CONFIG_AUTOCHECK, false);
			editor.putBoolean(CONFIG_AUTO2NEXT, false);
			editor.putBoolean(CONFIG_AUTO2ADDWRONGSET, false);
			editor.putBoolean(CONFIG_SOUND, false);
			editor.putBoolean(CONFIG_CHECKBYRANDOM, false);
			editor.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//Toast.makeText(this, "修改错误", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	/*
	 * 数据转入
	 */
	public void DataTrans() {
		InputStream in = getResources().openRawResource(R.raw.testsubject);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in, "gb2312"));// 注意编码
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			Log.e("debug", e1.toString());

		}
		String tmp, body;
		String TESTSUBJECT;
		String TESTANSWER;
		String ANSWERA;
		String ANSWERB;
		String ANSWERC;
		String ANSWERD;
		String IMAGENAME;

		int TESTTPYE;
		int TESTBELONG;
		int EXPR1;

		String[] strings = new String[13];
		ContentValues values = new ContentValues();
		try {
			DBAdapter dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			while ((tmp = br.readLine()) != null) {
				strings = tmp.split("#");

				TESTSUBJECT = strings[1];
				TESTANSWER = strings[2];
				TESTTPYE = Integer.parseInt(strings[3]);// int
				TESTBELONG = Integer.parseInt(strings[4]);// int
				ANSWERA = strings[5];
				ANSWERB = strings[6];
				ANSWERC = strings[7];
				ANSWERD = strings[8];
				IMAGENAME = "image" + strings[9];
				IMAGENAME.replace("-", "_");
				EXPR1 = Integer.parseInt(strings[10]);// int

				values.clear();
				values.put(DBAdapter.TESTSUBJECT, TESTSUBJECT);
				values.put(DBAdapter.TESTANSWER, TESTANSWER);
				values.put(DBAdapter.TESTTPYE, TESTTPYE);
				values.put(DBAdapter.TESTBELONG, TESTBELONG);
				values.put(DBAdapter.ANSWERA, ANSWERA);
				values.put(DBAdapter.ANSWERB, ANSWERB);
				values.put(DBAdapter.ANSWERC, ANSWERC);
				values.put(DBAdapter.ANSWERD, ANSWERD);
				values.put(DBAdapter.IMAGENAME, IMAGENAME);
				values.put(DBAdapter.EXPR1, EXPR1);

				dbAdapter.DBInsert(values);

				Log.i(tmp, tmp);
			}
			// tv.setText(body);
			br.close();
			in.close();
			body = "";
			Cursor cursor = dbAdapter.getAllData();
			body += cursor.getCount();
			// cursor.moveToFirst();
			// body+=cursor.getString(cursor.getColumnIndex(DBAdapter.IMAGENAME));
			// while(cursor.moveToNext()){
			// body += cursor.getString(1);
			// }
			// tv.setText(body);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("debug", e.toString());
		}
	}

}