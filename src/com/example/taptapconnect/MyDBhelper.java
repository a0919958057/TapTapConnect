package com.example.taptapconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBhelper extends SQLiteOpenHelper {

	// 資料庫名稱
	public static final String DATABASE_NAME = "mydata.db";
	// 資料庫版本，資料結構改變的時候更改數字
	public static final int VERSON = 1;
	// 資料庫物件
	private static SQLiteDatabase database;

	public MyDBhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public static SQLiteDatabase getDatabase(Context context) {
		//假如資料庫為空或是不開放讀取
		if (database == null || !database.isOpen()) {
			//建立新Database
			database = new MyDBhelper(context, DATABASE_NAME, null, VERSON)
					.getWritableDatabase();
		}
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//建立新表格
		db.execSQL(ItemDAO.CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//刪除原有表格
		db.execSQL("DROP TABLE IF EXISTS "+ItemDAO.TABLE_NAME);
		//建立新表格
		onCreate(db);
		
	}

}
