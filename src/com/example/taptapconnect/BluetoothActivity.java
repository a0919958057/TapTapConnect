package com.example.taptapconnect;

import com.example.taptapconnect.bluetoothservice.BluetoothService;
import com.example.taptapconnect.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BluetoothActivity extends Activity {

	protected static final String EXTRA_DEVICE_ADDRESS = "address_string";
	protected static final int RETURN_MAC_ADDRESS = 17;
	private static final String TAG = "BluetoothActivity";
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
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
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_textview);
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_textview);

		unpairButton = (Button) findViewById(R.id.button_bt_unpair);
		pairedButton = (Button) findViewById(R.id.button_bt_paired);
		mainListview = (ListView) findViewById(R.id.listview_bt);
		statusText = (TextView) findViewById(R.id.textview_status);
		mainListview.setAdapter(mPairedDevicesArrayAdapter);
		
		unpairButton.setOnClickListener(new ButtonListener());
		pairedButton.setOnClickListener(new ButtonListener());
		mainListview.setOnItemClickListener(mDeviceClickListener);
		
		
		for(BluetoothDevice btobject : (new BluetoothService()).getDevices()) {
			mPairedDevicesArrayAdapter.add(
					btobject.getName()+"\nMAC: "+btobject.getAddress()
			);
		}
		
		
		//TODO : 完成意圖過濾器
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
			//TODO : 增加搜尋功能
			
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
    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    
    
    
    /**
     * BroadcastReceiver會被呼叫當裝置被發現
     * 並且在搜尋結束時改變標題
     * 
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
    	
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\nMAC: " + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }

    };

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
    	if (mBtAdapter.getScanMode() !=
    			BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
    		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
    		startActivity(discoverableIntent);
    	}
    }
	
	public class ButtonListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.button_bt_paired:
				onPairedButton();
				break;

			case R.id.button_bt_unpair:
				onUnpairButton();
				break;
			}

		}

		private void onPairedButton() {
			statusText.setText(getResources().getString(R.string.textview_paired));
			mainListview.setAdapter(mPairedDevicesArrayAdapter);
			mPairedDevicesArrayAdapter.clear();
			for(BluetoothDevice btobject : (new BluetoothService()).getDevices()) {
				mPairedDevicesArrayAdapter.add(
						btobject.getName()+"\nMAC: "+btobject.getAddress()
				);
			}
			
		}

		private void onUnpairButton() {
			statusText.setText(getResources().getString(R.string.textview_unpair));
			mainListview.setAdapter(mNewDevicesArrayAdapter);
			mNewDevicesArrayAdapter.clear();
			doDiscovery();
		}

	}


}
