package ahscode.test01nd;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyRightDrawerFragment extends Fragment 
	implements MyDialogWithEditOkCancel.WithEditOkCancelListener{
	
	EditText mEd;
	private Button mB00;
	private Button mB01;
	public MyRightDrawerFragment() {
		//system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyRightDrawerFragment();
		f.setRetainInstance(true);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_rightdrawer,container,false);
		mEd = (EditText) v.findViewById(R.id.rd_edit00);
		mB00 = (Button) v.findViewById(R.id.rd_btn00);
		mB01 = (Button) v.findViewById(R.id.rd_btn01);
		mB00.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), mEd.getText(), Toast.LENGTH_SHORT).show();
			}
		});
		mB01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment df = MyDialogWithEditOkCancel.newInstance();
				String tag = "dialog_right";
				Bundle b = new Bundle();
				b.putString(tag, tag);
				df.setArguments(b);
				df.setTargetFragment(MyRightDrawerFragment.this, 0);
				df.show(getFragmentManager(), tag);
			}
		});
		return v;
	}

	@Override
	public void onDialogButtonClick(int which, String string) {
		this.mEd.setText(string);
		Toast.makeText(getActivity(), ""+which, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDialogCanceled(String message) {
		Toast.makeText(getActivity(), message+"でしたがキャンセルです", Toast.LENGTH_SHORT).show();
	}

}
