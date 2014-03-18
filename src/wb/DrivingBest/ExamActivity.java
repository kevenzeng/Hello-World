package wb.DrivingBest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ExamActivity extends Activity {

	public static final int TESTLIMIT = 100;
	int curIndex;
	String myAnswer;

	int[] myWAset = new int[900];// ��������
	int[] mySelect = new int[100];// �����¼
	int[] testTurn = new int[100];
	int[] problemTurn = new int[900];
	int[] testAnswer = new int[100];
	int resultInt;
	boolean isHandIn;// ��ʾ�����
	int minutes, seconds;

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
	Chronometer chronometer;

	Cursor cursor;
	DBAdapter dbAdapter;

	// InputStream in;
	// BufferedReader br;
	FileInputStream fis;
	FileOutputStream fos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.exerciselayout);
		Init();
		OnPaint();

		forword_btn.setOnClickListener(new OnClickListener() {
			// ��һ��
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curIndex == 0) {
					ShowToast("��ǰΪ��һ��");
				} else {
					if (isHandIn) {// �����
						int tindex = curIndex;
						while (--tindex >= 0) {
							if (mySelect[tindex] != testAnswer[tindex]) {
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
				if (curIndex == TESTLIMIT - 1) {
					ShowToast("��ǰΪ���һ��");
				} else {
					if (isHandIn) {
						int tindex = curIndex;
						while (++tindex < TESTLIMIT) {
							if (mySelect[tindex] != testAnswer[tindex]) {
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
		 * ����
		 */
		check_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHandInAgainDialog();
			}
		});

		/*
		 * ��������
		 */
		addWAset_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myWAset[testTurn[curIndex]] = 1;
				saveWaset();
				ShowToast("����ɹ�");
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
						if (!isHandIn) {
							if (radioA.isChecked() && mySelect[curIndex] != 1) {
								mySelect[curIndex] = 1;
								next_btn.performClick();
							} else if (radioB.isChecked()
									&& mySelect[curIndex] != 2) {
								mySelect[curIndex] = 2;
								next_btn.performClick();
							} else if (radioC.isChecked()
									&& mySelect[curIndex] != 3) {
								mySelect[curIndex] = 3;
								next_btn.performClick();
							} else if (radioD.isChecked()
									&& mySelect[curIndex] != 4) {
								mySelect[curIndex] = 4;
								next_btn.performClick();
							}
						}
					}
				});

		// ÿ��ʱ��ı�
		chronometer
				.setOnChronometerTickListener(new OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub
						seconds--;
						if (seconds == -1) {
							minutes--;
							seconds = 59;
						}
						if (minutes < 0) {
							chronometer.stop();
							// ֱ�ӽ���
							handlerAfterHandIn();
						} else {
							if (minutes < 5) {
								chronometer.setTextColor(Color.RED);
								chronometer.setText(nowtime());
							} else {
								chronometer.setTextColor(Color.GREEN);
								chronometer.setText(nowtime());
							}
						}
					}
				});
	}

	protected void showHandInAgainDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("ȷ�Ͻ�����");

		builder.setTitle("��ʾ");

		builder.setPositiveButton("ȷ��",
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface v, int which) {
						// TODO Auto-generated method stub
						handlerAfterHandIn();
					}
				});
		builder.setNegativeButton("ȡ��", null);
		builder.create().show();
	}

	// �������
	protected void handlerAfterHandIn() {
		// TODO Auto-generated method stub
		/*
		 * �ɼ�ͳ�� ����ͳ�� isHandIn��־�޸� ʱ���־�ĳɼ��������־ �������ɴ����������
		 */
		chronometer.setVisibility(View.GONE);
		addWAset_btn.setVisibility(View.VISIBLE);
		check_btn.setEnabled(false);
		isHandIn = true;

		String tmpanswer;
		for (int i = 99; i >= 0; i--) {
			cursor.moveToPosition(testTurn[i]);
			tmpanswer = cursor.getString(cursor
					.getColumnIndex(DBAdapter.TESTANSWER));
			if (tmpanswer.compareTo("��") == 0) {
				testAnswer[i] = 1;
			} else if (tmpanswer.compareTo("��") == 0) {
				testAnswer[i] = 3;
			} else if (tmpanswer.compareTo("A") == 0) {
				testAnswer[i] = 1;
			} else if (tmpanswer.compareTo("B") == 0) {
				testAnswer[i] = 2;
			} else if (tmpanswer.compareTo("C") == 0) {
				testAnswer[i] = 3;
			} else if (tmpanswer.compareTo("D") == 0) {
				testAnswer[i] = 4;
			}
			if (testAnswer[i] == mySelect[i]) {
				resultInt++;
			}
		}
		showScore();
		curIndex = 0;
		for (int i = 0; i < 99; i++) {
			if (mySelect[i] != testAnswer[i]) {
				curIndex = i;
				break;
			}
		}
		OnPaint();
	}

	private void showScore() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("���");
		if (resultInt == 100) {
			builder.setMessage("�ۣ����֣����������");
		} else if (resultInt >= 90) {
			builder.setMessage("�ϸ��ˣ��ɼ�Ϊ�� " + resultInt + ",���ٽ�����");
		} else {
			builder.setMessage("���ϸ񣡳ɼ�Ϊ��" + resultInt + ",�����Ŭ��");
		}
		builder.setPositiveButton("ȷ��", null);
		builder.create().show();
	}

	// ����ʱ����

	public void ShowToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void Init() {
		// TODO Auto-generated method stub
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
		chronometer = (Chronometer) findViewById(R.id.exam_chronometer);
		check_btn.setText("����");
		addWAset_btn.setVisibility(View.GONE);
		minutes = 45;
		seconds = 0;
		chronometer.setText(nowtime());
		chronometer.setVisibility(View.VISIBLE);
		chronometer.start();
		isHandIn = false;
		resultInt = 0;

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

		Random r = new Random();
		int t, rt1, rt2;
		for (int i = 0; i < 900; i++) {
			rt1 = r.nextInt(900);
			rt2 = r.nextInt(900);
			t = problemTurn[rt1];
			problemTurn[rt1] = problemTurn[rt2];
			problemTurn[rt2] = t;
		}

		curIndex = 0;
		try {
			dbAdapter = new DBAdapter(this);
			dbAdapter.open();
			cursor = dbAdapter.getAllData();
			int cnt = 0;
			for (int i = 0; cnt < 40; i++) {
				cursor.moveToPosition(problemTurn[i]);
				if (cursor.getInt(cursor.getColumnIndex(DBAdapter.TESTTPYE)) == 2) {// �ж���
					mySelect[cnt] = 0;
					testTurn[cnt++] = problemTurn[i];
				}
			}
			for (int i = 0; cnt < 100; i++) {
				cursor.moveToPosition(problemTurn[i]);
				if (cursor.getInt(cursor.getColumnIndex(DBAdapter.TESTTPYE)) == 1) {// ѡ����
					mySelect[cnt] = 0;
					testTurn[cnt++] = problemTurn[i];
				}
			}
		} catch (Exception e) {
			ShowToast(e.toString());
		}
	}

	// ʣ��ʱ��string
	private String nowtime() {
		// TODO Auto-generated method stub
		if (seconds < 10) {
			return (minutes + ":0" + seconds);
		} else {
			return (minutes + ":" + seconds);
		}
	}

	public void OnPaint() {
		if (cursor.getCount() == 0) {
			Toast.makeText(this, "fuck", Toast.LENGTH_LONG).show();
		} else {
			/*
			 * ��ʼ��View
			 */
			cursor.moveToPosition(testTurn[curIndex]);
			if (mySelect[curIndex] == 0) {
				radioGroup.clearCheck();
			}
			TESTSUBJECT = cursor.getString(cursor
					.getColumnIndex(DBAdapter.TESTSUBJECT));
			TESTSUBJECT = TESTSUBJECT.replace("��|��", "��ͼ");
			TESTANSWER = cursor.getString(cursor
					.getColumnIndex(DBAdapter.TESTANSWER));
			IMAGENAME = cursor.getString(cursor
					.getColumnIndex(DBAdapter.IMAGENAME));
			TESTTPYE = cursor.getInt(cursor.getColumnIndex(DBAdapter.TESTTPYE));
			proTextView.setText((curIndex + 1) + "." + TESTSUBJECT);
			// addWAset_btn.setText("���");

			if (!isHandIn) {
				promptText.setVisibility(View.GONE);
				promptText.setText("");
			} else {
				promptText.setVisibility(View.VISIBLE);
				promptText.setText("��ȷ��Ϊ: " + TESTANSWER);
				promptText.setTextSize(16);
				promptText.setTextColor(Color.RED);
			}
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

			switch (mySelect[curIndex]) {
			case 1:
				radioA.setChecked(true);
				break;
			case 2:
				radioB.setChecked(true);
				break;
			case 3:
				radioC.setChecked(true);
				break;
			case 4:
				radioD.setChecked(true);
				break;
			default:
				break;
			}
		}
	}

	/*
	 * ��������;
	 */
	public void saveWaset() {
		try {
			String text = "";
			fos = openFileOutput(WrongSetShowList.WAsetFilename, MODE_PRIVATE);
			for (int i = 0; i < 900; i++) {
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
		dbAdapter.close();
		super.onDestroy();
	}
}
