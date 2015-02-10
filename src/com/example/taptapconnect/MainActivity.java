package com.example.taptapconnect;

import java.util.ArrayList;
import java.util.List;

import com.example.taptapconnect.btDialogFragment.callbackDialog;
import com.example.taptapconnect.dotview.DotView;
import com.example.taptapconnect.dotview.DotView.CanvasTransformation;
import com.example.taptapconnect.gameobject.GameEvent;
import com.example.taptapconnect.gameobject.GameObject;
import com.example.taptapconnect.gameobject.GameObjectHandler;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.*;
import android.content.Intent;
import android.graphics.Canvas;
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

	protected static final int BT_REQUEST_ENABLE = 0;
	GameObjectHandler gamehandler1, gamehandler2, gamehandler3;
	List<GameObjectHandler> array = new ArrayList<GameObjectHandler>();
	DotView dotview;
	private BluetoothAdapter mBluetoothAdapter;
	DotGenerator dotg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actbar = getActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_main);
		dotview = new DotView(this);
		dotview.setBackgroundColor(Color.BLACK);
		((FrameLayout) findViewById(R.id.root2)).addView(dotview, 0);

		setupBluetooth();

		gamehandler1 = new GameObjectHandler();
		gamehandler2 = new GameObjectHandler();
		gamehandler3 = new GameObjectHandler();

		array.add(gamehandler1);
		array.add(gamehandler2);
		array.add(gamehandler3);

		dotg = new DotGenerator(gamehandler3, dotview, Color.YELLOW);

		Thread thread = new Thread(dotg);
		thread.start();

		for (GameObjectHandler object : array) {
			object.setListener(new ObjectsLinstener(object));
		}

		((Button) findViewById(R.id.button_dialog1))
				.setOnClickListener(new ButtonListener());
		((Button) findViewById(R.id.button_bt_connect))
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
		getMenuInflater().inflate(R.menu.manu_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			for (GameObjectHandler objects : array) {
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
			case R.id.button_dialog1:

				makeDot(gamehandler1, dotview, Color.RED);
				textview1.setText(String.valueOf(gamehandler1.peekGameObject()
						.getX()));
				textview2.setText(String.valueOf(gamehandler1.peekGameObject()
						.getY()));
				Log.i(this.getClass().getName(), "Button1 prass");
				break;
			case R.id.button_bt_connect:

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

				// TODO 記得刪除
				dotg.stop();

				Log.i(this.getClass().getName(), "Button3 prass");
				break;
			default:

			}

		}

	}

	class ObjectsLinstener implements GameObjectHandler.GameObjectListener {

		public GameObjectHandler linstenOnwer;

		public void setOnwer(GameObjectHandler onwer) {
			this.linstenOnwer = onwer;
		}

		public ObjectsLinstener() {
			this(null);
		}

		public ObjectsLinstener(GameObjectHandler onwer) {
			setOnwer(onwer);
		}

		@Override
		public void onChange(GameEvent event) {
			dotview.setTransformation(new CanvasTransformation() {

				@Override
				public void tranform(Canvas canvas) {
					canvas.translate(getModifyX(), getModifyY());
					canvas.rotate((float) (linstenOnwer.getCount() * -12));

				}

				@Override
				public String getString() {
					return null;
				}

				private float getModifyX() {
					double x = dotview.getWidth() / 2;
					double y = dotview.getHeight() / 2;
					return (float) (x + Math.sqrt(x * x + y * y)
							* (Math.cos(Math.PI - Math.atan(y / x)
									+ (linstenOnwer.getCount() * Math.PI / 15))));
				}

				private float getModifyY() {
					double x = dotview.getWidth() / 2;
					double y = dotview.getHeight() / 2;
					return (float) (y - Math.sqrt(x * x + y * y)
							* (Math.sin(Math.PI - Math.atan(y / x)
									+ (linstenOnwer.getCount() * Math.PI / 15))));
				}
			});
			dotview.invalidate();
		}

		@Override
		public void onEmpty(GameEvent event) {
		}

		@Override
		public boolean onMove(GameEvent event) {
			return false;
		}

		@Override
		public void onTouch(GameEvent event) {
		}

		@Override
		public void onDelete(GameEvent event) {
		}

	}

	public void makeDot(final GameObjectHandler dots, DotView view, int color) {

		dots.add(new GameObject((float) ((view.getWidth() / 2) + (view
				.getWidth() / 2)
				* (100.0 / (100.0 + (double) dots.getCount()))
				* Math.cos(dots.getCount() * Math.PI / 15)), (float) ((view
				.getHeight() / 2) + (view.getHeight() / 2)
				* (100.0 / (100.0 + (double) dots.getCount()))
				* Math.sin(dots.getCount() * Math.PI / 15)), color,
				(float) 10.0));

		view.setDotsArray(array);
	}

	private class DotGenerator implements Runnable {

		final String TAG = this.getClass().getName();
		volatile boolean isStop;
		private GameObjectHandler gameobjects;
		private DotView view;
		private int color;
		private Handler hdlr = new Handler();
		public Runnable makedots = new Runnable() {

			@Override
			public void run() {
				makeDot(gameobjects, view, color);

			}

		};

		DotGenerator(GameObjectHandler objectHandler, DotView v, int c) {
			this.gameobjects = objectHandler;
			this.view = v;
			this.color = c;
		}

		/**
		 * 停止DotGeneration的運作
		 */
		public void stop() {
			this.isStop = true;
		}

		/**
		 * 新執行緒之run()方法
		 * 
		 * 每 1000} 毫秒呼叫makedots一次
		 */
		@Override
		public void run() {
			while (!isStop) {
				try {
					Thread.sleep(1000);
					hdlr.post(makedots);
				} catch (java.lang.InterruptedException e) {
					Log.i(TAG, "DotGenerate stop!", e);
				}

			}

		}

	}

	void setupBluetooth() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			btDialogFragment.newInstance(this,
					btDialogFragment.DEVICES_NOT_ENABLE, 6).show();

			Log.e(this.getClass().getSimpleName(), "Blueteeth Not Support");
			return;
		} else if (!mBluetoothAdapter.isEnabled()) {
			btDialogFragment dialog;
			(dialog = btDialogFragment.newInstance(this,
					btDialogFragment.DEVICES_NOT_ENABLE, 6)).show();
			dialog.setCallback(new callbackDialog() {

				@Override
				public void callback() {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, BT_REQUEST_ENABLE);
				}

			});

		}
		startActivity(new Intent(this, BluetoothActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BT_REQUEST_ENABLE:
			if (resultCode == RESULT_OK) {
				// TODO:開始進行配對與搜尋

			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}

	}

}
