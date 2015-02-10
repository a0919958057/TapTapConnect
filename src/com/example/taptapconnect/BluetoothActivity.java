package com.example.taptapconnect;

import com.example.taptapconnect.bluetoothservice.BluetoothService;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BluetoothActivity extends Activity {

	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_textview);

		Button scanButton = (Button) findViewById(R.id.button_bt_pair);
		Button connectButton = (Button) findViewById(R.id.button_bt_connect);
		ListView mainListview = (ListView) findViewById(R.id.listview_bt);
		mainListview.setAdapter(mPairedDevicesArrayAdapter);
		for(BluetoothDevice btobject : (new BluetoothService()).getDevices()) {
			mPairedDevicesArrayAdapter.add(btobject.getName());
		}
		
	

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home :
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	

}
