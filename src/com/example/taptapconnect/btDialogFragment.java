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
import android.view.ViewGroup;

public class btDialogFragment extends DialogFragment {

	final static int DEVICES_NOT_SUPPORT = 0;
	final static int DEVICES_NOT_FOUND = 1;

	int mNum;
	private FragmentManager parentFM;
	private Activity parentActivity;
	private int dialogStatus;
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
	static btDialogFragment newInstance(Activity con, int status, int mNum) {
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

	public btDialogFragment( Activity con, int status) {
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
		return v;

	}

	public void setFragmentManager(FragmentManager fm) {
		this.parentFM = fm;
	}

	public boolean show() {
		if (parentFM != null) {
			this.show(parentFM, "BT_DIALOG");
			return true;
		} else {
			return false;
		}
	}

}