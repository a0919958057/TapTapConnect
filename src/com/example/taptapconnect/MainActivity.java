package com.example.taptapconnect;

import java.util.ArrayList;
import java.util.List;

import com.example.taptapconnect.btDialogFragment.callbackDialog;
import com.example.taptapconnect.bluetoothservice.BluetoothService;
import com.example.taptapconnect.bluetoothservice.BluetoothService.Constants;
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
import android.app.Service;
import android.bluetooth.*;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
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
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity {

	protected static final int BT_REQUEST_ENABLE = 0;

	// 用於Handler.callback 之資料查詢key
	public static final String BLUETEETH_REMOTE_PRASS = "remote_prass";
	public static final String BLUETEETH_REMOTE_UNPRASS = "remote_unprass";
	public static final String BLUETEETH_REMOTE_TOUCH = "remote_touch";

	public Activity mainActivity = this;

	private GameObjectHandler gamehandler1, gamehandler2, gamehandler3;
	private List<GameObjectHandler> array = new ArrayList<GameObjectHandler>();
	private DotView dotview;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothService mBTService;
	private DotGenerator dotg;
	private Button buttonRed;
	private Button buttonGreen;
	private Button buttonCount;
	private Vibrator mVibrator;

	volatile private boolean isRemoteOnPrassOff;
	volatile private boolean isRemoteOnPrass;
	volatile private boolean isRemoteOnTouch;

	public String mConnectedDeviceName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ActionBar actbar = getActionBar();
		// actbar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_main);
		dotview = new DotView(this);
		dotview.setBackgroundColor(Color.BLACK);
		((FrameLayout) findViewById(R.id.root2)).addView(dotview, 0);

		setupBluetooth();
		mVibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);

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

		(buttonRed = (Button) findViewById(R.id.button_dialog1))
				.setOnClickListener(new ButtonListener());
		(buttonGreen = (Button) findViewById(R.id.button_bt_paired))
				.setOnClickListener(new ButtonListener());
		(buttonCount = (Button) findViewById(R.id.button3))
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
		case R.id.action_bt_activity:
			startActivityForResult(new Intent(this, BluetoothActivity.class),
					BluetoothActivity.RETURN_MAC_ADDRESS);
			// TODO 這邊記得要做Intent優化

			// for (GameObjectHandler objects : array) {
			// objects.clear();
			// }
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
				// TODO 增加藍芽連接之控制項 此處為暫時
				sendMessage(BLUETEETH_REMOTE_TOUCH);
				// TODO 此為測試記得刪除
				mVibrator.vibrate(50);

				Log.i(this.getClass().getName(), "Button1 prass");
				break;
			case R.id.button_bt_paired:

				makeDot(gamehandler2, dotview, Color.GREEN);
				textview1.setText(String.valueOf(gamehandler2.peekGameObject()
						.getX()));
				textview2.setText(String.valueOf(gamehandler2.peekGameObject()
						.getY()));
				// TODO 此為測試記得刪除
				mVibrator.vibrate(100);
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
		volatile boolean isStop = true;
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
		 * 開始DotGeneration的運作
		 */
		public void start() {
			this.isStop = false;
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
		mBTService = new BluetoothService(new Handler(new UIMessageHander()));
		startActivity(new Intent(this, BluetoothActivity.class));

		// TODO 記得這邊有需要完善的Intent，建議新建method來進行
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
			break;
		case BluetoothActivity.RETURN_MAC_ADDRESS:
			connectDevice(data);
			Log.i(getClass().getSimpleName(), "GET MAC ADDRESS SUCCEFUL");
		}
	}

	/**
	 * Establish connection with other divice
	 *
	 * @param data
	 *            An {@link Intent} with
	 *            {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
	 * @param secure
	 *            Socket Security type - Secure (true) , Insecure (false)
	 */
	private void connectDevice(Intent data) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				BluetoothActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mBTService.connect(device);
	}

	/**
	 * Updates the status on the action bar.
	 *
	 * @param resId
	 *            a string resource ID
	 */
	private void setStatus(int status) {

		final ActionBar actionBar = getActionBar();
		if (null == actionBar) {
			return;
		}
		actionBar.setSubtitle(status);

		// if (status == 0) {
		// actionBar.setSubtitle(R.string.bt_unpaired);
		// }
		// if (status == 1) {
		// actionBar.setSubtitle(getResources().getString(R.string.bt_paired)
		// + " : " + BluetoothService.getPairedID());
		// }

	}

	private void setStatus(String strStatus) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(strStatus);
	}

	class UIMessageHander implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {

			Bundle data;
			data = msg.getData();

			isRemoteOnPrass = data.getBoolean(BLUETEETH_REMOTE_PRASS);
			isRemoteOnPrassOff = data.getBoolean(BLUETEETH_REMOTE_UNPRASS);
			isRemoteOnTouch = data.getBoolean(BLUETEETH_REMOTE_TOUCH);

			Activity activity = mainActivity;
			switch (msg.what) {
			case Constants.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to)
							+ mConnectedDeviceName);
					break;
				case BluetoothService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case Constants.MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				Log.i("WRITE MESSAGE", "successful!");

				break;
			case Constants.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				if (readMessage.equals(BLUETEETH_REMOTE_TOUCH)) {
					buttonGreen.performClick();
				}
				Log.i("READ MESSAGE", "successful!");
				Log.i("READ MESSAGE", readMessage);
				break;
			case Constants.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(
						Constants.DEVICE_NAME);
				if (null != activity) {
					Toast.makeText(activity,
							"Connected to " + mConnectedDeviceName,
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constants.MESSAGE_TOAST:
				if (null != activity) {
					Toast.makeText(activity,
							msg.getData().getString(Constants.TOAST),
							Toast.LENGTH_SHORT).show();
				}
				break;
			}

			return false;
		}

	}

	/**
	 * 向遠端裝置發出訊息
	 * 
	 * @param message
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mBTService.getState() != mBTService.STATE_CONNECTED) {
			Toast.makeText(mainActivity, R.string.title_not_connected,
					Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mBTService.write(send);

		}
	}

}
