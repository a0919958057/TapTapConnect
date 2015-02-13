package com.example.taptapconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBhelper extends SQLiteOpenHelper {

	// ��Ʈw�W��
	public static final String DATABASE_NAME = "mydata.db";
	// ��Ʈw�����A��Ƶ��c���ܪ��ɭԧ��Ʀr
	public static final int VERSON = 1;
	// ��Ʈw����
	private static SQLiteDatabase database;

	public MyDBhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public static SQLiteDatabase getDatabase(Context context) {
		//���p��Ʈw���ũάO���}��Ū��
		if (database == null || !database.isOpen()) {
			//�إ߷sDatabase
			database = new MyDBhelper(context, DATABASE_NAME, null, VERSON)
					.getWritableDatabase();
		}
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�إ߷s���
		db.execSQL(ItemDAO.CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//�R���즳���
		db.execSQL("DROP TABLE IF EXISTS "+ItemDAO.TABLE_NAME);
		//�إ߷s���
		onCreate(db);
		
	}

}
