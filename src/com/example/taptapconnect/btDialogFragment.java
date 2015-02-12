package com.example.taptapconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

public class btDialogFragment extends DialogFragment {

	final static int DEVICES_NOT_SUPPORT = 0;
	final static int DEVICES_NOT_ENABLE = 1;

	int mNum;
	private FragmentManager parentFM;
	private MainActivity parentActivity;
	private int dialogStatus;
	private callbackDialog callback;
	private volatile static String[] strarrays;
	private volatile static Resources res;

	/**
	 * 建立一個Dialog實例
	 * 
	 * @param fm
	 *            父活動之FragmentManager
	 * @param str
	 *            設置該活動之類別
	 * @param nNum
	 *            設置DialogFragment之主題
	 */
	
	
	static btDialogFragment newInstance(MainActivity con, int status, int mNum) {
		btDialogFragment f = new btDialogFragment(con, status);
		res = con.getResources();
		strarrays = res.getStringArray(R.array.dialog_status);
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("themeIndex", mNum);
		f.setArguments(args);
		return f;
	}

	static String getStatusString(int index) {
		return ((strarrays == null) ? "strarray == null" : strarrays[index]);
	}

	public btDialogFragment( MainActivity con, int status) {
		this.dialogStatus = status;
		this.parentActivity = con;
		parentFM = con.getFragmentManager();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("themeIndex");
		res = parentActivity.getResources();
		strarrays = res.getStringArray(R.array.dialog_status);
		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		switch ((mNum - 1) % 6) {
		case 1:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 2:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 3:
			style = DialogFragment.STYLE_NO_INPUT;
			break;
		case 4:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 5:
			style = DialogFragment.STYLE_NORMAL;
			break;
		case 6:
			style = DialogFragment.STYLE_NO_TITLE;
			break;
		case 7:
			style = DialogFragment.STYLE_NO_FRAME;
			break;
		case 8:
			style = DialogFragment.STYLE_NORMAL;
			break;
		}
		switch ((mNum - 1) % 6) {
		case 4:
			theme = android.R.style.Theme_Holo;
			break;
		case 5:
			theme = android.R.style.Theme_Holo_Light_Dialog;
			break;
		case 6:
			theme = android.R.style.Theme_Holo_Light;
			break;
		case 7:
			theme = android.R.style.Theme_Holo_Light_Panel;
			break;
		case 8:
			theme = android.R.style.Theme_Holo_Light;
			break;
		}
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// getDialog().requestWindowFeature(STYLE_NO_TITLE);
		getDialog().setTitle(getStatusString(dialogStatus));
		View v = inflater.inflate(R.layout.dialog_fragment, container);
		Button b1,b2;
		b1 = (Button)v.findViewById(R.id.button_red);
		b2 = (Button)v.findViewById(R.id.button_dialog2);
		switch(dialogStatus){
		case DEVICES_NOT_ENABLE :
			b2.setText(res.getString(R.string.button_bt_enable));
			b1.setText(res.getString(R.string.button_bt_cancel));
			b2.setOnClickListener(new dialogListener());
			b1.setOnClickListener(new dialogListener());
			break;
		case DEVICES_NOT_SUPPORT :
			b2.setAlpha((float) 0.0);
			b2.setClickable(false);
			b1.setText(res.getString(R.string.button_bt_enable));
			v.findViewById(R.id.button_red).
			setOnClickListener(new dialogListener());
			break;
		}
		return v;

	}

	public void setFragmentManager(FragmentManager fm) {
		this.parentFM = fm;
	}
	
	public class dialogListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.button_red :
				parentActivity.finish();
				break;
			case R.id.button_dialog2 :
				callback.callback();
				dismiss();
			}
			
		}
	}
	

	public boolean show() {
		if (parentFM != null) {
			this.show(parentFM, "BT_DIALOG");
			return true;
		} else {
			return false;
		}
	}
	public void setCallback(callbackDialog c){
		this.callback = c;
	}

	static interface callbackDialog {
		void callback();
	}
}