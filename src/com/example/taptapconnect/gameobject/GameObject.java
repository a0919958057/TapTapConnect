package com.example.taptapconnect.gameobject;

import android.graphics.Color;
import android.util.Log;

public class GameObject {
	private String gameId;
	private float X, Y;
	private int color;
	private float diameter;
	Boolean isVisable;

	public GameObject(float xSet, float ySet) {
		this(xSet, ySet, Color.BLACK, (float)5.0);
		
	}
	
	public GameObject(float xSet, float ySet, int c, float d) {
		this.X = xSet;
		this.Y = ySet;
		this.color = c;
		this.diameter = d;
		Log.i(this.getClass().getName(), "GameObject Create!");
	}

	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId
	 *            the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return X;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		X = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return Y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		Y = y;
	}

	public int getColor() {
		return this.color;
	}

	public float getDiameter() {
		return this.diameter;
	}
}
