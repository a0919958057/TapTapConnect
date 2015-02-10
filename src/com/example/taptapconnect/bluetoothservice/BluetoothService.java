package com.example.taptapconnect.bluetoothservice;

import java.util.Set;
import java.util.UUID;

import android.bluetooth.*;

public class BluetoothService extends Thread {
	// DEBUG標籤
	private static final String TAG = "BluetoothService";
	// 藍芽UUID
	private static final UUID uuid = UUID
			.fromString("8E5579F0-785B-44B3-BCE9-BCF8F0F157FA");
	// 數據管理
	static BluetoothAdapter mBluetoothAdapter;
	static Set<BluetoothDevice> btDevices;
	

	// 建構子
	public BluetoothService() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		btDevices = mBluetoothAdapter.getBondedDevices();
	}

	/**
	 * 取得裝置列表
	 */
	public Set<BluetoothDevice> getDevices() {

		return btDevices;

	}
	

}
