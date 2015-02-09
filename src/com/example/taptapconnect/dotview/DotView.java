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
	 *            �C���s�����}�C
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
		//�x�s�����ɤ�Canvas
		canvas.save();
		
		//TODO �ݭn��X�i�h��Drawable����
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
		
		//�^�_�|���ק蠟Canvas
		canvas.restore();
	}

	private CanvasTransformation cvasTf;

	/**
	 * �]�wCanvasTransformation
	 * 
	 * @param canvasTransformation
	 *            ��@CanvasTransformation������
	 */

	public void setTransformation(CanvasTransformation canvasTransformation) {
		this.cvasTf = canvasTransformation;
	}

	/**
	 * �ޥγ]�m�L��CanvasTransformation.tranform��k�ק�޼Ƥ�����
	 * �p�GCanvasTransformation����null�hdo nothing
	 * 
	 * @param canvas
	 *            �ݭק諸Canvas����
	 */
	private void modifyCanvas(Canvas canvas) {
		if (cvasTf != null) {
			this.cvasTf.tranform(canvas);
		}

	}

}
