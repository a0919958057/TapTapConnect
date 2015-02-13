package com.example.taptapconnect;

import com.example.taptapconnect.MainActivity;
import com.example.taptapconnect.bluetoothservice.BluetoothService;
import com.example.taptapconnect.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothActivity extends Activity {

	protected static final String EXTRA_DEVICE_ADDRESS = "address_string";
	protected static final int RETURN_MAC_ADDRESS = 17;
	private static final String TAG = "BluetoothActivity";
	private DeviceListAdapter mNewDevicesArrayAdapter;
	private DeviceListAdapter mPairedDevicesArrayAdapter;
	private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

	private Button unpairButton;
	private Button pairedButton;
	private ListView mainListview;
	private TextView statusText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mPairedDevicesArrayAdapter = (DeviceListAdapter) new DeviceListAdapter(
				this, R.layout.device_itemview);
		mNewDevicesArrayAdapter = (DeviceListAdapter) new DeviceListAdapter(
				this, R.layout.device_itemview);

		unpairButton = (Button) findViewById(R.id.button_bt_unpair);
		pairedButton = (Button) findViewById(R.id.button_green);
		mainListview = (ListView) findViewById(R.id.listview_bt);
		statusText = (TextView) findViewById(R.id.textview_status);
		mainListview.setAdapter(mPairedDevicesArrayAdapter);

		unpairButton.setOnClickListener(new ButtonListener());
		pairedButton.setOnClickListener(new ButtonListener());
		mainListview.setOnItemClickListener(mDeviceClickListener);

		for (BluetoothDevice btobject : (new BluetoothService()).getDevices()) {
			mPairedDevicesArrayAdapter.add(btobject.getName()
					+ btobject.getAddress());
		}

		// TODO : 完成意圖過濾器
		// 註冊廣播過濾器 : 當偵測到裝置時
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// 註冊廣播過濾器 : 當裝置偵測完畢時
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.action_bt_search:
			doDiscovery();
			ensureDiscoverable();
			break;
		// TODO : 增加搜尋功能

		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Make sure we're not doing discovery anymore
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}

		// Unregister broadcast listeners
		this.unregisterReceiver(mReceiver);
		setResult(Activity.RESULT_CANCELED);
	}

	/**
	 * Start device discover with the BluetoothAdapter
	 */
	private void doDiscovery() {
		Log.i(TAG, "doDiscovery()");

		// Indicate scanning in the title
		setTitle(R.string.scanning);

		// If we're already discovering, stop it
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}

		// Request discover from BluetoothAdapter
		mBtAdapter.startDiscovery();
	}

	/**
	 * The on-click listener for all devices in the ListViews
	 */
	private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			// Cancel discovery because it's costly and we're about to connect
			mBtAdapter.cancelDiscovery();

			// Get the device MAC address, which is the last 17 chars in the
			// View

			String address = (String) ((TextView) (v
					.findViewById(R.id.itemview_text2))).getText();

			// Create the result Intent and include the MAC address
			Intent intent = new Intent();
			intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

			// Set result and finish this Activity
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	};

	/**
	 * BroadcastReceiver會被呼叫當裝置被發現 並且在搜尋結束時改變標題
	 * 
	 * The BroadcastReceiver that listens for discovered devices and changes the
	 * title when discovery is finished
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mNewDevicesArrayAdapter.add(device.getName()
							+ device.getAddress());
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				setTitle(R.string.select_device);
				if (mNewDevicesArrayAdapter.getCount() == 0) {
					String noDevices = getResources().getText(
							R.string.none_found).toString();
					mNewDevicesArrayAdapter.add(noDevices);
				}
			}
		}

	};

	/**
	 * Makes this device discoverable.
	 */
	private void ensureDiscoverable() {
		if (mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	public class ButtonListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.button_green:
				onPairedButton();
				break;

			case R.id.button_bt_unpair:
				onUnpairButton();
				break;
			}

		}

		private void onPairedButton() {
			statusText.setText(getResources().getString(
					R.string.textview_paired));
			mainListview.setAdapter(mPairedDevicesArrayAdapter);
			mPairedDevicesArrayAdapter.clear();
			for (BluetoothDevice btobject : (new BluetoothService())
					.getDevices()) {
				mPairedDevicesArrayAdapter.add(btobject.getName()
						+ btobject.getAddress());
			}

		}

		private void onUnpairButton() {
			statusText.setText(getResources().getString(
					R.string.textview_unpair));
			mainListview.setAdapter(mNewDevicesArrayAdapter);
			mNewDevicesArrayAdapter.clear();
			doDiscovery();
		}

	}

}

class DeviceListAdapter extends ArrayAdapter<String> {

	private static final String LIST_VIEW_ADAPTER = "ListViewAdapter";
	private Context mContext;
	private int mResourse;
	private String deviceInfo;
	private String deviceName;
	private String deviceMac;

	public DeviceListAdapter(Context context, int resource) {
		super(context, resource);
		mContext = context;
		mResourse = resource;

	}

	/**
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemView;
		if (getCount() > 0) {
			deviceInfo = getItem(position);
		} else {
			deviceInfo = "";
		}
		if (deviceInfo.length() >= 17) {
			deviceMac = deviceInfo.substring(deviceInfo.length() - 17);
			deviceName = deviceInfo.substring(0, deviceInfo.length() - 17);
		} else {
			deviceMac = "";
			deviceName = "";
		}
		// 當convertView==Null時將建立新的view
		if (convertView == null) {

			itemView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater lf = (LayoutInflater) getContext().getSystemService(
					inflater);
			lf.inflate(mResourse, itemView, true);

		} else {
			itemView = (LinearLayout) convertView;
		}

		//取得mResourse內的布局
		TextView tvDeviceName = (TextView) itemView
				.findViewById(R.id.itemview_text1);
		TextView tvDeviceMAC = (TextView) itemView
				.findViewById(R.id.itemview_text2);
		ImageView iv = (ImageView) itemView
				.findViewById(R.id.imageview_bluetooth);
		tvDeviceName.setText(deviceName);
		tvDeviceMAC.setText(deviceMac);
		Log.i(LIST_VIEW_ADAPTER, MainActivity.getDeviceName() +deviceName+ "0");

		// 當取得之DeviceName不為零時
		if (MainActivity.getDeviceName() != null) {
			Log.i(LIST_VIEW_ADAPTER, MainActivity.getDeviceName() + "1");
			
			if (MainActivity.getDeviceName().equals(deviceName)) {
				Log.i(LIST_VIEW_ADAPTER, MainActivity.getDeviceName() + "2");
				iv.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.ic_launcher));
				itemView.setBackgroundColor(mContext.getResources().getColor(R.color.green));
			} else {
				itemView.setBackgroundColor(Color.TRANSPARENT);
				iv.setImageDrawable(mContext.getResources().getDrawable(
						R.drawable.ic_action_bluetooth_searching));
			}
		} else {
			iv.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.ic_action_bluetooth_searching));
			itemView.setBackgroundColor(Color.TRANSPARENT);
		}

		return itemView;

	}

}
