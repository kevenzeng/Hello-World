package wb.DrivingBest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionActivity extends Activity {

	CheckBox chk_autocheck;
	CheckBox chk_auto2next;
	CheckBox chk_auto2addWAset;
	CheckBox chk_sound;
	CheckBox chk_checkbyrdm;

	Button btn_saveSetting;
	Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionlayout);

		chk_autocheck = (CheckBox) findViewById(R.id.chk_autocheck);
		chk_auto2next = (CheckBox) findViewById(R.id.chk_auto2next);
		chk_auto2addWAset = (CheckBox) findViewById(R.id.chk_auto2addWAset);
		chk_sound = (CheckBox) findViewById(R.id.chk_sound);
		chk_checkbyrdm = (CheckBox) findViewById(R.id.chk_checkbyrdm);

		btn_saveSetting = (Button) findViewById(R.id.btn_savesetting);
		btn_return = (Button) findViewById(R.id.btn_return);
		//CONGIG初始化
		configInit();
		btn_saveSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveSettingAction();
				finish();
			}
		});
		
		btn_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void saveSettingAction(){
		try {
			SharedPreferences sharedPreferences = getSharedPreferences(
					DrivingBest.PREFERENCE_NAME, DrivingBest.MODE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(DrivingBest.CONFIG_AUTOCHECK, chk_autocheck.isChecked());
			editor.putBoolean(DrivingBest.CONFIG_AUTO2NEXT, chk_auto2next.isChecked());
			editor.putBoolean(DrivingBest.CONFIG_AUTO2ADDWRONGSET, chk_auto2addWAset.isChecked());
			editor.putBoolean(DrivingBest.CONFIG_SOUND, chk_sound.isChecked());
			editor.putBoolean(DrivingBest.CONFIG_CHECKBYRANDOM, chk_checkbyrdm.isChecked());
			editor.commit();
			ShowToast("保存配置成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ShowToast("保存配置错误");
			e.printStackTrace();
		}
	}
	public void ShowToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void configInit() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences(
				DrivingBest.PREFERENCE_NAME, DrivingBest.MODE);
		chk_autocheck.setChecked(sharedPreferences.getBoolean(DrivingBest.CONFIG_AUTOCHECK, false));
		chk_auto2next.setChecked(sharedPreferences.getBoolean(DrivingBest.CONFIG_AUTO2NEXT, false));
		chk_auto2addWAset.setChecked(sharedPreferences.getBoolean(DrivingBest.CONFIG_AUTO2ADDWRONGSET, false));
		chk_sound.setChecked(sharedPreferences.getBoolean(DrivingBest.CONFIG_SOUND, false));
		chk_checkbyrdm.setChecked(sharedPreferences.getBoolean(DrivingBest.CONFIG_CHECKBYRANDOM, false));
	}
}
