package wb.DrivingBest;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExerciseActivity extends Activity {

	public static final int problemLimit = 900;
	public static final String label = "label";
	int curIndex;
	String myAnswer;
	// int[] WAset = new int[900];// ��ǰ����
	int[] myWAset = new int[900];// ��������
	// String[] mySelect = new String[900];// �����¼
	int[] problemTurn = new int[900];
	int Option;// ��ʾ����� or ˳��
	int labelProblemID;

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

	boolean autoCheck;
	boolean auto2next;
	boolean auto2addWAset;
	EditText editText;
	TextView proTextView;
	ImageView imageview;
	RadioButton radioA;
	RadioButton radioB;
	RadioButton radioC;
	RadioButton radioD;
	RadioGroup radioGroup;
	Button forword_btn;
	Button next_btn;
	Button check_btn;
	Button addWAset_btn;
	TextView promptText;

	Cursor cursor;
	DBAdapter dbAdapter;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	// InputStream in;
	// BufferedReader br;
	FileInputStream fis;
	FileOutputStream fos;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exerciselayout);

		Init();// ͼ�����ݳ�ʼ��
		settingInit();// �����趨
		OnPaint();// �ػ�
		forword_btn.setOnClickListener(new OnClickListener() {
			// ��һ��
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curIndex == 0) {
					ShowToast("��ǰΪ��һ��");
				} else {
					if (Option == DrivingBest.OPTION_WRONGEXERCISE) {
						int tindex = curIndex;
						while (--tindex >= 0) {
							if (myWAset[tindex] == 1) {
								curIndex = tindex;
								OnPaint();
								return;
							}
						}
						ShowToast("��ǰΪ��һ��");
						return;
					} else {
						curIndex--;
						OnPaint();
					}
				}
			}
		});
		/*
		 * ��һ��
		 */
		next_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curIndex == problemLimit - 1) {
					ShowToast("��ǰΪ���һ��");
				} else {
					if (Option == DrivingBest.OPTION_WRONGEXERCISE) {
						int tindex = curIndex;
						while (++tindex < problemLimit) {
							if (myWAset[tindex] == 1) {
								curIndex = tindex;
								OnPaint();
								return;
							}
						}
						ShowToast("��ǰΪ���һ��");
						return;
					} else {
						curIndex++;
						OnPaint();
					}
				}
			}
		});

		/*
		 * ȷ�ϰ�ť
		 */
		check_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int answerNum = radioGroup.getCheckedRadioButtonId();
				switch (answerNum) {
				case R.id.radioA:
					myAnswer = "A";
					break;
				case R.id.radioB:
					myAnswer = "B";
					break;
				case R.id.radioC:
					myAnswer = "C";
					break;
				case R.id.radioD:
					myAnswer = "D";
					break;
				case -1:
					myAnswer = "";
				default:
					myAnswer = "";
					break;
				}
				if (TESTTPYE == 2) {
					if (myAnswer == "A") {
						myAnswer = "��";
					} else if(myAnswer == "C"){
						myAnswer = "��";
					}
				}
				// ShowToast(myAnswer + " " + TESTANSWER);
				if (myAnswer.compareTo(TESTANSWER) == 0) {
					promptText.setText(R.string.prompt_right);
					promptText.setVisibility(View.VISIBLE);
					promptText.setTextColor(Color.GREEN);
					if (auto2next) {
						next_btn.performClick();
					}
				} else {
					promptText.setText(R.string.prompt_wrong);
					promptText.setText(promptText.getText().toString()
							+ TESTANSWER);
					promptText.setVisibility(View.VISIBLE);
					promptText.setTextColor(Color.RED);
					if (Option != DrivingBest.OPTION_WRONGEXERCISE
							&& auto2addWAset) {
						addWAset_btn.performClick();
					}
				}
			}
		});

		/*
		 * ��������
		 */
		addWAset_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Option == DrivingBest.OPTION_WRONGEXERCISE) {
					myWAset[problemTurn[curIndex]] = 0;
					saveWaset();
					ShowToast("�Ƴ��ɹ�");
				} else {
					myWAset[problemTurn[curIndex]] = 1;
					ShowToast("����ɹ�");
				}
			}
		});

		/*
		 * ѡ��radio
		 */
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						if (autoCheck
								&& (radioA.isChecked() || radioB.isChecked()
										|| radioC.isChecked() || radioD
										.isChecked())) {
							check_btn.performClick();
						}
					}

				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (Option == DrivingBest.OPTION_ORDER) {
			menu.add(0, 1, 1, "��ת��ָ�����");
			menu.add(0, 2, 2, "��ת����ǩ");
			menu.add(0, 3, 3, "��Ϊ��ǩ");
		}
		menu.add(0, 4, 4, "����");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			ShowDialog2JumpByIndex();
		} else if (item.getItemId() == 2) {
			jumpAction(labelProblemID);
		} else if (item.getItemId() == 3) {
			labelProblemID = curIndex + 1;
		} else if (item.getItemId() == 4) {
			startActivity(new Intent().setClass(ExerciseActivity.this,
					OptionActivity.class));
			// settingInit();//ע���ǲ��в����ģ�����˵���ǵȻ�������ִ�еģ�Ȼ���еģ��������
			// ����Ӧ����restartʱ��ִ�вŶԡ�
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean jumpAction(int jump2ID) {
		if (jump2ID > 0 && jump2ID <= 900) {
			curIndex = jump2ID - 1;
			OnPaint();
			return true;
		} else {
			return false;
		}

	}

	public void ShowDialog2JumpByIndex() {
		editText = new EditText(this);
		// editText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		// editText.setInputType("number");
		editText.setKeyListener(new DigitsKeyListener(false, true));
		editText.setHint("������Ҫ��ת�����");
		new AlertDialog.Builder(this)
				.setTitle("������")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(editText)
				.setPositiveButton("ȷ��",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								if (!jumpAction(Integer.parseInt(editText
										.getText().toString()))) {
									ShowToast("ָ����Ų�����");
								}
							}
						}).setNegativeButton("ȡ��", null).show();
	}

	public void ShowToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	public void Init() {
		Bundle bundle = getIntent().getExtras();
		Option = bundle.getInt("option");
		try {
			dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			sharedPreferences = getSharedPreferences(
					DrivingBest.PREFERENCE_NAME, DrivingBest.MODE);// SharedPreferences�洢��ʽ
			editor = sharedPreferences.edit();
			// SharedPreferences.Editor editor = sharedPreferences.edit();

		} catch (Exception e) {
			// TODO: handle exception
			Log.i("Init", "WA");
		}
		proTextView = (TextView) findViewById(R.id.pro_text);
		imageview = (ImageView) findViewById(R.id.imageview);
		radioA = (RadioButton) findViewById(R.id.radioA);
		radioB = (RadioButton) findViewById(R.id.radioB);
		radioC = (RadioButton) findViewById(R.id.radioC);
		radioD = (RadioButton) findViewById(R.id.radioD);
		forword_btn = (Button) findViewById(R.id.forwordBtn);
		next_btn = (Button) findViewById(R.id.nextBtn);
		check_btn = (Button) findViewById(R.id.checkBtn);
		addWAset_btn = (Button) findViewById(R.id.addWAsetBtn);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		promptText = (TextView) findViewById(R.id.promptText);

		for (int i = 0; i < 900; i++) {
			// WAset[i] = 0;
			// mySelect[i] = "";
			problemTurn[i] = i;
		}

		/*
		 * ������Ŀ��ȡ
		 */
		try {
			String Text = "";
			fis = openFileInput(WrongSetShowList.WAsetFilename);
			byte[] readBytes = new byte[fis.available()];
			while (fis.read(readBytes) != -1) {
				Text = new String(readBytes);
			}
			String[] tmp_waset = Text.split("#");
			String tmpString;
			if (tmp_waset[0].compareTo("") != 0) {
				for (int i = 0; i < tmp_waset.length; i++) {
					tmpString = tmp_waset[i].substring(0,
							tmp_waset[i].indexOf('.'));
					// ShowToast(tmpString);
					myWAset[Integer.parseInt(tmpString) - 1] = 1;

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// ShowToast(e.toString());
		}

		if (Option == DrivingBest.OPTION_RDM) {
			Random r = new Random();
			int t, rt1, rt2;
			for (int i = 0; i < 900; i++) {
				rt1 = r.nextInt(900);
				rt2 = r.nextInt(900);
				t = problemTurn[rt1];
				problemTurn[rt1] = problemTurn[rt2];
				problemTurn[rt2] = t;
			}
		}
		curIndex = 0;
		if (Option == DrivingBest.OPTION_WRONGEXERCISE) {
			curIndex = bundle.getInt("startfrom");
			addWAset_btn.setText("�Ƴ������");
		}
		cursor = dbAdapter.getAllData();
		Log.i("Count", cursor.getCount() + "");
	}

	public void settingInit() {
		autoCheck = sharedPreferences.getBoolean(DrivingBest.CONFIG_AUTOCHECK,
				false);// �Զ�ȷ�ϣ��������ļ���ȡ��
		labelProblemID = sharedPreferences.getInt(label, 0);// ��ʱ��������ļ���
		auto2next = sharedPreferences.getBoolean(DrivingBest.CONFIG_AUTO2NEXT,
				false);
		auto2addWAset = sharedPreferences.getBoolean(
				DrivingBest.CONFIG_AUTO2ADDWRONGSET, false);
	}

	public void OnPaint() {
		if (cursor.getCount() == 0) {
			Toast.makeText(this, "fuck", Toast.LENGTH_LONG).show();
		} else {
			/*
			 * ��ʼ��View
			 */
			cursor.moveToPosition(problemTurn[curIndex]);
			radioGroup.clearCheck();
			TESTSUBJECT = cursor.getString(cursor
					.getColumnIndex(DBAdapter.TESTSUBJECT));
			TESTSUBJECT = TESTSUBJECT.replace("��|��", "��ͼ");
			TESTANSWER = cursor.getString(cursor
					.getColumnIndex(DBAdapter.TESTANSWER));
			IMAGENAME = cursor.getString(cursor
					.getColumnIndex(DBAdapter.IMAGENAME));
			TESTTPYE = cursor.getInt(cursor.getColumnIndex(DBAdapter.TESTTPYE));
			proTextView
					.setText((problemTurn[curIndex] + 1) + "." + TESTSUBJECT);
			// addWAset_btn.setText("���");

			promptText.setVisibility(View.GONE);
			promptText.setText("");
			// Toast.makeText(this, IMAGENAME+"--"+IMAGENAME.length(),
			// Toast.LENGTH_LONG).show();

			// ͼƬ����
			if (IMAGENAME.compareTo("image") != 0) {
				InputStream inputStream;
				try {
					IMAGENAME = IMAGENAME.replace('-', '_');
					// Toast.makeText(this, IMAGENAME,
					// Toast.LENGTH_LONG).show();
					inputStream = super.getAssets().open(IMAGENAME);
					imageview.setImageDrawable(Drawable.createFromStream(
							inputStream, "assets"));
					imageview.setVisibility(View.VISIBLE);
					// imageview.setImageDrawable(Drawable.createFromPath("res.drawable."+IMAGENAME+".jpg"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
			} else {
				imageview.setVisibility(View.GONE);
			}
			ANSWERA = cursor
					.getString(cursor.getColumnIndex(DBAdapter.ANSWERA));
			ANSWERB = cursor
					.getString(cursor.getColumnIndex(DBAdapter.ANSWERB));
			ANSWERC = cursor
					.getString(cursor.getColumnIndex(DBAdapter.ANSWERC));
			ANSWERD = cursor
					.getString(cursor.getColumnIndex(DBAdapter.ANSWERD));
			if (ANSWERA.compareTo("") == 0) {
				// �ж���
				radioA.setText("��");
				radioC.setText("��");
				radioB.setVisibility(View.INVISIBLE);
				radioD.setVisibility(View.INVISIBLE);
			} else {
				// ѡ����
				radioA.setText("A." + ANSWERA);
				radioB.setText("B." + ANSWERB);
				radioC.setText("C." + ANSWERC);
				radioD.setText("D." + ANSWERD);
				radioA.setVisibility(View.VISIBLE);
				radioB.setVisibility(View.VISIBLE);
				radioC.setVisibility(View.VISIBLE);
				radioD.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		settingInit();
		super.onRestart();
	}

	public void saveWaset() {
		try {
			String text = "";
			fos = openFileOutput(WrongSetShowList.WAsetFilename, MODE_PRIVATE);
			for (int i = 0; i < problemLimit; i++) {
				if (myWAset[i] == 1) {
					// cursor.moveToPosition(i);
					cursor.moveToPosition(i);
					text += (i + 1)
							+ "."
							+ cursor.getString(cursor
									.getColumnIndex(DBAdapter.TESTSUBJECT))
							+ "#";
				}
			}
			if (text.compareTo("") == 0)
				text = "#";
			fos.write(text.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			// ShowToast(e.toString());
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// ��������
		saveWaset();
		dbAdapter.close();
		super.onDestroy();
	}
}
