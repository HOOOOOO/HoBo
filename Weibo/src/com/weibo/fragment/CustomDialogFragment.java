package com.weibo.fragment;

import com.example.weibo.R;
import com.weibo.tools.MyApplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class CustomDialogFragment extends DialogFragment implements OnClickListener{
	
	private DialogClickListener listener;
	
	public static CustomDialogFragment newInstance(String str1, String str2) {
		CustomDialogFragment dialogFragment = new CustomDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("choose1", str1);
		bundle.putString("choose2", str2);
		dialogFragment.setArguments(bundle);
		return dialogFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment, container);
		((Button)view.findViewById(R.id.choose1)).setText(getArguments().getString("choose1"));
		((Button)view.findViewById(R.id.choose1)).setOnClickListener(this);
		((Button)view.findViewById(R.id.choose2)).setText(getArguments().getString("choose2"));
		((Button)view.findViewById(R.id.choose2)).setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
		getDialog().getWindow().setLayout((int) (MyApplication.mWidthOfScreen*0.8), LayoutParams.WRAP_CONTENT);//这2行,和上面的一样,注意顺序就行;
		//getDialog().getWindow().setBackgroundDrawableResource(android.R.color.holo_red_dark);
		//getDialog().getWindow().setLayout(MyApplication.mWidthOfScreen, (int) (MyApplication.mHeightOfScreen*0.4));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.choose1:
			listener.choose(1);
			dismiss();
			
			break;
		case R.id.choose2:
			listener.choose(2);
			dismiss();
		default:
			break;
		}
	}
	
	public void setListener(DialogClickListener listener) {
		this.listener = listener;
	}
	
	public interface DialogClickListener{
		public void choose(int p);
	}

}
