package ahscode.test01nd;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyTodayFragment extends Fragment
	implements
	MainActivity.MyDrawerEventListener
	,MyFullCustomDialogFragment.EventPassFromCustomDialogListener{

	private String mTitle;
	private boolean mFlgLeftDrawrOpen;
	private boolean mFlgRightDrawrOpen;
	private EditText mEd;
	private Button mBtn00;
	private Button mBtn01;

	public MyTodayFragment() {
		//system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyTodayFragment();
		f.setRetainInstance(true);
		return f;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mTitle = this.getArguments().getString(this.getResources().getString(R.string.selected_tytle));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_layout_today, menu);
	}
	//callback-MainActivity.MyDrawerEventListener
	@Override
	public void changeLeftDrawerOpenFlg(boolean flg) {
		mFlgLeftDrawrOpen = flg;
	}
	@Override
	public void changeRightDrawerOpenFlg(boolean flg) {
		mFlgRightDrawrOpen = flg;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() != android.R.id.home){
			if((this.mFlgRightDrawrOpen == false) && (this.mFlgLeftDrawrOpen == false)){
				Toast.makeText(getActivity(), item.getTitle() +this.mTitle+"today" , Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if((this.mFlgRightDrawrOpen == false) && (this.mFlgLeftDrawrOpen == false)){
			this.getActivity().getActionBar().setTitle(mTitle);
			menu.setGroupVisible(R.id.menugroup_today, true);
		}else{
			menu.setGroupVisible(R.id.menugroup_today, false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_todayfragment, container, false);
		mEd = (EditText)v.findViewById(R.id.today_ed00);
		mBtn00 = (Button)v.findViewById(R.id.today_btn00);
		mBtn01 = (Button)v.findViewById(R.id.today_btn01);
		mBtn00.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), mEd.getText(), Toast.LENGTH_SHORT).show();
			}
		});
		mBtn01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment df = MyFullCustomDialogFragment.newInstance();
				df.setTargetFragment(MyTodayFragment.this, 0);
				df.show(getFragmentManager(), "contentsDialog");
			}
		});
		return v;
	}

	@Override
	public void clickPositive() {
		mEd.setText("positive");
		Toast.makeText(getActivity(), "okボタン", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void clickClose() {
		Toast.makeText(getActivity(), "キャンセルでした", Toast.LENGTH_SHORT).show();
	}
}
