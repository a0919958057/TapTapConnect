package com.example.taptapconnect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.graphics.Color;

public class MapItem {

	private static final int DATA_CHANGE_STATUS = 4;
	private static final int DATA_CHANGE_CONTENT = 3;
	private static final int DATA_CHANGE_POSITION = 2;
	private static final int DATA_CHANGE_COLOR = 1;
	private static final int DATA_CHANGE_DATE = 0;
	// 預設設定值內容
	public static final int COLOR_DEFAULT = Color.GREEN;
	public static final double POSITION_X_DEAFULT = 0.0;
	public static final double POSITION_Y_DEAFULT = 0.0;
	public static final String CONTENT_DEAFULT = "";
	// 物件內含資料
	private int color;
	private double positionX;
	private double positionY;
	private String content;
	private int status;

	// 當內容發生改變的時候呼叫
	private void notifyChange(int dataChangeEvent) {
		switch (dataChangeEvent) {
		case DATA_CHANGE_DATE:
			break;
		case DATA_CHANGE_COLOR:
			break;
		case DATA_CHANGE_POSITION:
			break;
		case DATA_CHANGE_CONTENT:
			break;
		case DATA_CHANGE_STATUS:
			break;
		}

	}

	// 表格內資料欄位
	private String datetime;
	private long itemId;

	/**
	 * @return the datetime
	 */
	public String getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime
	 *            the datetime to set
	 */
	public void setDatetime(String datetime) {
		notifyChange(DATA_CHANGE_DATE);
		this.datetime = datetime;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(int color) {
		notifyChange(DATA_CHANGE_COLOR);
		this.color = color;
	}

	/**
	 * 
	 * @return position[] 回傳X Y座標
	 */
	public double[] getPosition() {
		double[] result = { 0, 0 };
		result[0] = this.positionX;
		result[1] = this.positionY;
		return result;
	}

	/**
	 * @param pX
	 *            the positionX to set
	 * @param pY
	 *            th positionY to set
	 */
	public void setPosition(double pX, double pY) {
		this.positionX = pX;
		this.positionY = pY;
		notifyChange(DATA_CHANGE_POSITION);
	}

	/**
	 * @return the context
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContent(String context) {
		this.content = context;
		notifyChange(DATA_CHANGE_CONTENT);
	}

	/**
	 * @return the sTATUS
	 */
	public int getSTATUS() {
		return status;
	}

	/**
	 * @param sTATUS
	 *            the sTATUS to set
	 */
	public void setSTATUS(int sTATUS) {
		status = sTATUS;
		notifyChange(DATA_CHANGE_STATUS);
	}

	/**
	 * @param c
	 *            色彩
	 * @param pX
	 *            經度座標
	 * @param pY
	 *            緯度座標
	 */
	public MapItem(int c, double pX, double pY) {
		SimpleDateFormat mformat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
		datetime = mformat.format(new Date(System.currentTimeMillis()));
		positionX = pX;
		positionY = pY;
		if (pX == 0.0 || pY == 0.0) {
			positionX = POSITION_X_DEAFULT;
			positionY = POSITION_Y_DEAFULT;
		}
		if (c == -1) {
			color = COLOR_DEFAULT;
		}
		color = c;
	}

	interface ImapItemWritable {

		MapItem insert(MapItem item);

		boolean update(MapItem item);

		boolean delete(long id);

		boolean deleteAll();
	}

	interface ImapItemReadable {

		List<MapItem> getAll();

		MapItem get(long id);

		MapItem getRecord(Cursor cursor);

		int getCount();

	}
	
	interface ImapItemDAO extends ImapItemWritable, ImapItemReadable {		
	}

	public void setId(long id) {
		this.itemId = id;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return this.itemId;
	}
}
