package com.example.taptapconnect.bluetoothservice;

import java.util.Set;
import java.util.UUID;

import android.bluetooth.*;

public class BluetoothService extends Thread {
	// DEBUG����
	private static final String TAG = "BluetoothService";
	// �Ū�UUID
	private static final UUID uuid = UUID
			.fromString("8E5579F0-785B-44B3-BCE9-BCF8F0F157FA");
	// �ƾں޲z
	static BluetoothAdapter mBluetoothAdapter;
	static Set<BluetoothDevice> btDevices;
	

	// �غc�l
	public BluetoothService() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		btDevices = mBluetoothAdapter.getBondedDevices();
	}

	/**
	 * ���o�˸m�C��
	 */
	public Set<BluetoothDevice> getDevices() {

		return btDevices;

	}
	

}
