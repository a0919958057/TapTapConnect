package com.example.taptapconnect.dotview;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.taptapconnect.gameobject.GameObject;
import com.example.taptapconnect.gameobject.GameObjectHandler;

/**
 * I see spots!
 *
 * @author ET
 */
public class DotView extends View {

	private volatile List<GameObjectHandler> dotsArray;

	/**
	 * @param context
	 *            the rest of the application
	 */
	public DotView(Context context) {
		super(context);
		setFocusableInTouchMode(true);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusableInTouchMode(true);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFocusableInTouchMode(true);
	}

	/**
	 * @param a ¹CÀ¸¸s¶°¤§°}¦C
	 */
	public void setDotsArray(List<GameObjectHandler> a) {

		this.dotsArray = a;

	}

	/**
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	Paint paint = new Paint();
	GameObject old;
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Style.STROKE);
		paint.setColor(hasFocus() ? Color.BLUE : Color.GRAY);
		canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, paint);
		if (null == dotsArray) {
			return;
		}
		for(GameObjectHandler dots : dotsArray){
			for (GameObject dot : dots) {
				if(old != null){
					paint.setStyle(Style.FILL);
					paint.setColor(dot.getColor());
					paint.setStrokeWidth((float)4.0);
					canvas.drawLine(old.getX(),
							old.getY(),
							dot.getX(),
							dot.getY(), paint);
				}
				paint.setStyle(Style.FILL);
				paint.setColor(dot.getColor());
				canvas.drawCircle(dot.getX(), dot.getY(), dot.getDiameter(), paint);
				old = dot;
				
			}
			old = null;
		}
	}

}
