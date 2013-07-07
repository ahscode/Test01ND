package ahscode.test01nd;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MySettingFragment extends Fragment implements MainActivity.MyDrawerEventListener{
	
	private String mTitle;
	private boolean mFlgLeftDrawrOpen ;
	private boolean mFlgRightDrawrOpen;
	
	public MySettingFragment() {
	}

	public static Fragment newInstance() {
		Fragment f = new MySettingFragment();
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
		inflater.inflate(R.menu.menu_layout_setting, menu);
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
			if(mTitle == null){
				mTitle = this.getArguments().getString(this.getResources().getString(R.string.selected_tytle));
			}
			this.getActivity().getActionBar().setTitle(mTitle);
			menu.setGroupVisible(R.id.menugroup_today, true);
		}else{
			menu.setGroupVisible(R.id.menugroup_today, false);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_settingfragment, container, false);
		return v;
	}
}
