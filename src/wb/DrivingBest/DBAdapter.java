package wb.DrivingBest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



/**
 *这个方法是关于数据库的操作
 * @author wb
 *
 */
//private final static String DATABASE_CREATE = "create table " + DATABSE_TABLE + " (TestID integer primary key autoincrement,"  
//+ "TestSubject text not null, TestAnswer text not null, TestType integer,TestBelong integer,AnswerA text," +
//		"AnswerB text,AnswerC text,AnswerD text,ImageName text,Expr1 integer);"; 

public class DBAdapter {
	public final static String DATABSE_TABLE = "TestSubject";
	public static final String TESTID = "TestID";
	public static final String TESTSUBJECT = "TestSubject";
	public static final String TESTANSWER = "TestAnswer";
	public static final String ANSWERA = "AnswerA";
	public static final String ANSWERB = "AnswerB";
	public static final String ANSWERC = "AnswerC";
	public static final String ANSWERD = "AnswerD";
	
	public static final String TESTTPYE = "TestType";
	public static final String TESTBELONG = "TestBelong";
	public static final String EXPR1 = "Expr1";
	public static final String IMAGENAME = "ImageName";
	
	
	
	
//一个DataBaseHelper类实例
	private DBHelper dataBaseHelper;
	//Context
	private Context context;
	//SQLiteDatabase;
	SQLiteDatabase sqLiteDatabase;
	
	
	public DBAdapter(Context context){
		//获得上下文信息context
		this.context = context;
	}
	/*
	 * Open the Database;
	 */
	public void open(){
		dataBaseHelper = new DBHelper(context);
		try {
			sqLiteDatabase = dataBaseHelper.getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			sqLiteDatabase = dataBaseHelper.getReadableDatabase();
			Log.i("open-->",e.toString());
		}
	}
	 // Close the Database
	public void close(){
		sqLiteDatabase.close();
	}
	
	/*
	 * 
	 */
	public long DBInsert(ContentValues cv){
		return sqLiteDatabase.insert(DBAdapter.DATABSE_TABLE,null,cv);
	}
	
	 public Cursor getAllData(){  
		 	//System.out.println("ASD");
	        String[] searchResult =  {TESTID,TESTSUBJECT, TESTANSWER,TESTTPYE, TESTBELONG
	        		,ANSWERA,ANSWERB,ANSWERC,ANSWERD,IMAGENAME,EXPR1};  
//	        Cursor tcursor = sqLiteDatabase.query(dataBaseHelper.DATABSE_TABLE, searchResult, null, null, null, null, null); 
//	        System.out.println(tcursor.getString(tcursor
//					.getColumnIndex(NotepadDbAdapter.TITLE)));
//	        System.out.println(tcursor.getString(tcursor
//					.getColumnIndex(NotepadDbAdapter.CREATED)));
	        Log.i("GetAllData","YES");
	        return sqLiteDatabase.query(dataBaseHelper.DATABSE_TABLE, searchResult, null, null, null, null, null);   
	 
	//	 String searchSQL = "select id , title , body ,created from "+ dataBaseHelper.DATABSE_TABLE;  
		// System.out.println(searchSQL);
	    //   	    Cursor tcursor = sqLiteDatabase.rawQuery(searchSQL, null);  
    //  tcursor.moveToFirst();
	      //    System.out.println(tcursor.getString(tcursor
			//			.getColumnIndex(NotepadDbAdapter.TITLE)));
		   //     System.out.println(tcursor.getString(tcursor
				//		.getColumnIndex(NotepadDbAdapter.CREATED)))
	       //   return  tcursor;;
		 } 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	*//**
	 * 返回nowtime
	 * @return
	 *//*
	public String nowtime(){
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR)+"/"
		+ calendar.get(Calendar.MONTH) + "/"
		+ calendar.get(Calendar.DAY_OF_MONTH) + " "
		+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
		+ calendar.get(Calendar.MINUTE);
		return created;
	}
	*//**
	 * 创建一篇日记
	 * @param title
	 * @param body
	 * @return
	 *//*
	public long createNote(String title,String body){
		ContentValues content = new ContentValues();
		content.put(TITLE, title);
		content.put(BODY, body);
		content.put(CREATED, nowtime());
		//return the row ID of the newly inserted row, or -1 if an error occurred 
		return sqLiteDatabase.insert(dataBaseHelper.DATABSE_TABLE, null, content);
	}
	*//**
	 * 删除一篇日记
	 * @param rowId
	 * @return
	 *//*
	 public boolean deleteNote(long rowId){   
	        String whereClause = ROWID + "=" + rowId;   
	       // return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. 
	        return sqLiteDatabase.delete(dataBaseHelper.DATABSE_TABLE, whereClause, null)>0;   
	 }  
	 
	 *//**
	  * 获取所有日记 返回的是游标集Cursor
	  * @return
	  *//*
	 public Cursor getAllNotes(){  
		 	//System.out.println("ASD");
	        String[] searchResult =  {ROWID, TITLE,BODY, CREATED};  
//	        Cursor tcursor = sqLiteDatabase.query(dataBaseHelper.DATABSE_TABLE, searchResult, null, null, null, null, null); 
//	        System.out.println(tcursor.getString(tcursor
//					.getColumnIndex(NotepadDbAdapter.TITLE)));
//	        System.out.println(tcursor.getString(tcursor
//					.getColumnIndex(NotepadDbAdapter.CREATED)));
        
	        return sqLiteDatabase.query(dataBaseHelper.DATABSE_TABLE, searchResult, null, null, null, null, null);   
	 
		 String searchSQL = "select id , title , body ,created from "+ dataBaseHelper.DATABSE_TABLE;  
		 System.out.println(searchSQL);
	       	    Cursor tcursor = sqLiteDatabase.rawQuery(searchSQL, null);  
         tcursor.moveToFirst();
	          System.out.println(tcursor.getString(tcursor
						.getColumnIndex(NotepadDbAdapter.TITLE)));
		        System.out.println(tcursor.getString(tcursor
						.getColumnIndex(NotepadDbAdapter.CREATED)))
	          return  tcursor;;
		 }   
	 
	 *//**
	  *返回的是特定编号的日记
	  * @param rowId
	  * @return
	  * @throws SQLException
	  *//*
	 public Cursor getNote(long rowId)throws SQLException{
		 String[] searchResult =  {ROWID, TITLE,BODY, CREATED};   
	        String whereString = ROWID + "=" + rowId;   
	        //查询参数令人发指
	        //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) 
	        Cursor mCursor = sqLiteDatabase.query(true, dataBaseHelper.DATABSE_TABLE, searchResult, whereString, null, null, null, null, null);   
	        if(mCursor!=null){   
	            mCursor.moveToFirst();   
	        }      
	        return mCursor;   
	    } 
	 
	 *//**
	  * 
	  * @param rowId
	  * @param title
	  * @param body
	  * @return the number of rows affected 
	  *//*
	 public boolean updateNote(long rowId ,String title,String body){   
         
	        ContentValues values = new ContentValues();   
	        values.put(ROWID, rowId);
	        values.put(TITLE, title);
	        values.put(BODY,body);   
	           
	        values.put(CREATED, nowtime());   
	        String whereString = ROWID + "=" + rowId;   
	        //public int update (String table, ContentValues values, String whereClause, String[] whereArgs) 
	        //return the number of rows affected 
	        return sqLiteDatabase.update(dataBaseHelper.DATABSE_TABLE, values, whereString, null)>0;   
	    }	
	 *//**
	  * 新建日记
	  * @param title
	  * @param body
	  *//*
	 public void _newNote(String title,String body){          
	        String insertSQL = "INSERT INTO " + dataBaseHelper.DATABSE_TABLE    
	            +"(" + ROWID +"," + TITLE+"," + BODY +"," + CREATED + ")"    
	            + " values (?,?,?,?)" ;//问号是占位符 
	        Object[] args = {null,title,body,nowtime()};//分别为上面四个内容   
	        sqLiteDatabase.execSQL(insertSQL, args); //插入操作
	 }
	 *//**
	  * 编辑日记
	  * @param rowId
	  * @param title
	  * @param body
	  *//*
	 public void editNote(long rowId,String title,String body){
		 String updateSQL = "update " + dataBaseHelper.DATABSE_TABLE    
         +" set " + TITLE+"=? ," + BODY +"=? ," + CREATED + "=? "    
         + " where " + ROWID + "= ?" ;   
	     Object[] args = {title,body,nowtime(),rowId};   
	     sqLiteDatabase.execSQL(updateSQL, args);  
	 }
	 *//**
	  * 删除日记
	  * @param rowId
	  *//*
	 public void delNote(long rowId){
		  String deleteSQL = "delete from "+ dataBaseHelper.DATABSE_TABLE +" where " + ROWID + "= ?" ;   
	        Object[] args = {rowId};   
	        sqLiteDatabase.execSQL(deleteSQL, args);   
	 }
	*/ 
}
