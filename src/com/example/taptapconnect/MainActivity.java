package com.example.taptapconnect;

import java.util.ArrayList;
import java.util.List;

import com.example.taptapconnect.dotview.DotView;
import com.example.taptapconnect.gameobject.GameEvent;
import com.example.taptapconnect.gameobject.GameObject;
import com.example.taptapconnect.gameobject.GameObjectHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity {

	GameObjectHandler gamehandler1, gamehandler2, gamehandler3;
	List<GameObjectHandler> array = new ArrayList<GameObjectHandler>();
	DotView dotview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		dotview = new DotView(this);
		dotview.setBackgroundColor(Color.BLACK);
		((FrameLayout) findViewById(R.id.root2)).addView(dotview, 0);

		gamehandler1 = new GameObjectHandler();
		gamehandler2 = new GameObjectHandler();
		gamehandler3 = new GameObjectHandler();
		
		array.add(gamehandler1);
		array.add(gamehandler2);
		array.add(gamehandler3);
		
		Thread thread = new Thread(new DotGenerator(gamehandler3, dotview, Color.YELLOW));
		thread.start();
		
		for(GameObjectHandler object : array) {
			object.setListener(new ObjectsLinstener());
		}
			

		((Button) findViewById(R.id.button1))
				.setOnClickListener(new ButtonListener());
		((Button) findViewById(R.id.button2))
				.setOnClickListener(new ButtonListener());
		((Button) findViewById(R.id.button3))
				.setOnClickListener(new ButtonListener());
		dotview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gamehandler3.add(new GameObject(event.getX(), event.getY(),
						Color.WHITE, (float) 2.0));
				dotview.setDotsArray(array);
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.manu_clear:
			for(GameObjectHandler objects : array){
				objects.clear();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	class ButtonListener implements OnClickListener {
		EditText textview1 = (EditText) findViewById(R.id.editText1);
		EditText textview2 = (EditText) findViewById(R.id.editText2);

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:

				makeDot(gamehandler1, dotview, Color.RED);
				textview1.setText(String.valueOf(gamehandler1.peekGameObject()
						.getX()));
				textview2.setText(String.valueOf(gamehandler1.peekGameObject()
						.getY()));
				Log.i(this.getClass().getName(), "Button1 prass");
				break;
			case R.id.button2:

				makeDot(gamehandler2, dotview, Color.GREEN);
				textview1.setText(String.valueOf(gamehandler2.peekGameObject()
						.getX()));
				textview2.setText(String.valueOf(gamehandler2.peekGameObject()
						.getY()));
				Log.i(this.getClass().getName(), "Button2 prass");
				break;
			case R.id.button3:
				textview1.setText(String.valueOf(gamehandler1.getCount()));
				textview2.setText(String.valueOf(gamehandler2.getCount()));
				Log.i(this.getClass().getName(), "Button3 prass");
				break;
			default:

			}

		}

	}
	
	class ObjectsLinstener implements GameObjectHandler.GameObjectListener {

		@Override
		public void onChange(GameEvent event) {
			dotview.invalidate();
		}

		@Override
		public void onEmpty(GameEvent event) {}

		@Override
		public boolean onMove(GameEvent event) {return false;}

		@Override
		public void onTouch(GameEvent event) {}

		@Override
		public void onDelete(GameEvent event) {}

		}

	public void makeDot(GameObjectHandler dots, DotView view, int color) {

		dots.add(new GameObject(
				(float) ((view.getWidth()/2)+
						(view.getWidth()/2)*
						(100.0/(100.0+(double)dots.getCount()))*
						Math.cos(dots.getCount()*Math.PI/15)),
				(float) ((view.getHeight()/2)+
						(view.getHeight()/2)*
						(100.0/(100.0+(double)dots.getCount()))*
						Math.sin(dots.getCount()*Math.PI/15)),
				color, (float) 10.0));
		view.setDotsArray(array);
	}
	
	private class DotGenerator implements Runnable {
		
		final String TAG = this.getClass().getName();
		volatile boolean isStop;
		private GameObjectHandler gameobjects;
		private DotView view;
		private int color;
		private Handler hdlr = new Handler();
		public Runnable makedots = new Runnable(){

			@Override
			public void run() {
				makeDot(gameobjects, view, color);
				
			}
			
		};
		
		DotGenerator(GameObjectHandler objectHandler, DotView v,int c) {
			this.gameobjects = objectHandler;
			this.view = v;
			this.color = c;
		}
		/**
		 * °±¤îDotGenerationªº¹B§@
		 */
		public void stop() {
			this.isStop = false;
		}

		@Override
		public void run() {
			while(!isStop) {
				try {
					Thread.sleep(1000);
					hdlr.post(makedots);
				} catch(java.lang.InterruptedException e) {
					Log.i(TAG, "DotGenerate stop!", e);
				}
				
			}
			
		}
		
	}
}
