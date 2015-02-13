package com.example.taptapconnect;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemDAO implements MapItem.ImapItemDAO {

	// ���W��
	public static final String TABLE_NAME = "item";

	// ��ƽs��
	public static final String KEY_ID = "_id";

	// ��椺������
	public static final String DATETIME_COLUME = "datatime";
	public static final String COLOR_COLUME = "color";
	public static final String POSITION_X_COLUME = "position_x";
	public static final String POSITION_Y_COLUME = "position_y";
	public static final String CONTENT_COLUME = "content";
	public static final String STATUS_COLUME = "status";

	/**
	 * �ϥΤW�������إߪ��
	 * 
	 * ���0:_id ���1:_datetime ���2:color ���3:positionX ���4:positionY ���5:content
	 * ���6:status
	 */
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DATETIME_COLUME + " TEXT NOT NULL, " + COLOR_COLUME
			+ " INTEGER NOT NULL, " + POSITION_X_COLUME + " REAL NOT NULL, "
			+ POSITION_Y_COLUME + " REAL NOT NULL, " + CONTENT_COLUME
			+ " TEXT, " + STATUS_COLUME + " INTEGER) ";
	// ��Ʈw����
	private SQLiteDatabase db;

	// �غc�l
	public ItemDAO(Context context) {
		db = MyDBhelper.getDatabase(context);
	}

	// ������Ʈw
	public void close() {
		db.close();
	}

	@Override
	public MapItem insert(MapItem item) {

		// �[�JContentValues�����ƥ]��
		ContentValues cValues = new ContentValues();
		cValues.put(COLOR_COLUME, item.getColor());
		cValues.put(POSITION_X_COLUME, item.getPosition()[0]);
		cValues.put(POSITION_Y_COLUME, item.getPosition()[1]);
		cValues.put(DATETIME_COLUME, item.getDatetime());
		cValues.put(CONTENT_COLUME, item.getContent());
		cValues.put(STATUS_COLUME, item.getSTATUS());

		// �[�J���Ʈw���A�è��o�s��id
		long id = db.insert(TABLE_NAME, null, cValues);
		item.setId(id);

		return item;

	}

	@Override
	public boolean update(MapItem item) {

		// �[�JContentValues�����ƥ]��
		ContentValues cValues = new ContentValues();
		cValues.put(COLOR_COLUME, item.getColor());
		cValues.put(POSITION_X_COLUME, item.getPosition()[0]);
		cValues.put(POSITION_Y_COLUME, item.getPosition()[1]);
		cValues.put(DATETIME_COLUME, item.getDatetime());
		cValues.put(CONTENT_COLUME, item.getContent());
		cValues.put(STATUS_COLUME, item.getSTATUS());

		String where = KEY_ID + "=" + item.getId();

		return db.update(TABLE_NAME, cValues, where, null) > 0;
	}

	@Override
	public boolean delete(long id) {
		String where = KEY_ID + "=" + id;
		return db.delete(TABLE_NAME, where, null) > 0;
	}

	@Override
	public List<MapItem> getAll() {
		List<MapItem> itemList = new ArrayList<MapItem>();

		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);

		while (cursor.moveToNext()) {
			itemList.add(getRecord(cursor));
		}
		cursor.close();

		return itemList;
	}

	@Override
	public MapItem get(long id) {
		String where = KEY_ID + "=" + id;
		MapItem result = null;
		Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			result = getRecord(cursor);
		}
		cursor.close();
		return result;
	}

	/**
	 * Ū��Cursor����
	 * 
	 * ���0:_id ���1:_datetime ���2:color ���3:positionX ���4:positionY ���5:content
	 * ���6:status
	 */
	@Override
	public MapItem getRecord(Cursor cursor) {
		MapItem result = new MapItem(0, 0, 0);
		result.setId(cursor.getLong(0));
		result.setDatetime(cursor.getString(1));
		result.setColor(cursor.getInt(2));
		result.setPosition(cursor.getDouble(3), cursor.getDouble(4));
		result.setContent(cursor.getString(5));
		result.setSTATUS(cursor.getInt(6));
		return result;
	}

	@Override
	public int getCount() {
		int result = 0;

		Cursor rawC = db.rawQuery("SELECT COUNT(*) FORM " + TABLE_NAME, null);

		while (rawC.moveToNext()) {
			result = rawC.getInt(0);
		}

		return result;

	}

}