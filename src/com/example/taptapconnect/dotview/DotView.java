package com.example.taptapconnect.dotview;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
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

	public interface CanvasTransformation {

		public void tranform(Canvas canvas);

		public String getString();

	}

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
	 * @param a
	 *            遊戲群集之陣列
	 */
	public void setDotsArray(List<GameObjectHandler> a) {

		this.dotsArray = a;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec,int heighMeasureSpec) {
		setMeasuredDimension(measuredSpec(widthMeasureSpec), measuredSpec(heighMeasureSpec));
		
	}

	private int measuredSpec(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if(specMode == MeasureSpec.EXACTLY) {
			return specSize;
		} else if(specMode == MeasureSpec.AT_MOST) {
			return specSize;
		} else {
			return 0;
		}
				
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
		//儲存未更改時之Canvas
		canvas.save();
		
		//TODO 需要整合進去為Drawable物件
		//////////////////////////////////////////////////////////
		modifyCanvas(canvas);
		if (null == dotsArray) {
			return;
		}
		for (GameObjectHandler dots : dotsArray) {
			for (GameObject dot : dots) {
				if (old != null) {
					paint.setStyle(Style.FILL);
					paint.setColor(dot.getColor());
					paint.setStrokeWidth((float) 4.0);
					canvas.drawLine(old.getX(), old.getY(), dot.getX(),
							dot.getY(), paint);
				}
				paint.setStyle(Style.FILL);
				paint.setColor(dot.getColor());
				canvas.drawCircle(dot.getX(), dot.getY(), dot.getDiameter(),
						paint);
				old = dot;

			}
			old = null;
		}
		//////////////////////////////////////////////////////////
		
		//回復尚未修改之Canvas
		canvas.restore();
	}

	private CanvasTransformation cvasTf;

	/**
	 * 設定CanvasTransformation
	 * 
	 * @param canvasTransformation
	 *            實作CanvasTransformation之物件
	 */

	public void setTransformation(CanvasTransformation canvasTransformation) {
		this.cvasTf = canvasTransformation;
	}

	/**
	 * 引用設置過的CanvasTransformation.tranform方法修改引數之物件
	 * 如果CanvasTransformation物件為null則do nothing
	 * 
	 * @param canvas
	 *            需修改的Canvas物件
	 */
	private void modifyCanvas(Canvas canvas) {
		if (cvasTf != null) {
			this.cvasTf.tranform(canvas);
		}

	}

}
