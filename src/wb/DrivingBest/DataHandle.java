package wb.DrivingBest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import android.content.ContentValues;
import android.content.Context;



public class DataHandle {
	/**
	 * TODO : ��ȡ�ļ�access
	 * 
	 * @param filePath
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Context context;
	public DataHandle(Context context){
		//�����������Ϣcontext
		this.context = context;
	}
	public static void DataHandler(Context context) {
		
		

		
		// List<Map> maplist= new ArrayList();
		Properties prop = new Properties();
		prop.put("charSet", "gb2312"); // �����ǽ����������
		prop.put("user", "localhost");
		prop.put("password", "wInd@r03O4");
		String filepath = "H:\\�㽭ʡ������ʻ����ƿ���\\data\\DriverBest.mdb";
		String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+ filepath; // �ļ���ַ
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			System.out.println("1");
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			System.out.println("2");
			Connection conn = DriverManager.getConnection(url, prop);
			stmt = (Statement) conn.createStatement();

			rs = stmt.executeQuery("select * from TestTemp");
			ResultSetMetaData data = rs.getMetaData();
			
			ContentValues cv = new ContentValues();
			DBAdapter dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			while (rs.next()) {
			//	Map map = new HashMap();
				cv.clear();
				for (int i = 2; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnName(i); // ����
					String columnValue = rs.getString(i);
					//map.put(columnName, columnValue);
					cv.put(columnName, columnValue);
					System.out.println(columnName);
				}
				//dbAdapter.DBInsert(cv);
				//System.out.println(columnName);
				// maplist.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
		// return null;
	}
}
