package ahscode.test01nd;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyDrawerLeftFragment extends Fragment{
	interface MyListViewItemClickListener{
		void connectableItemPosition(int position);
	}
	private ListView mlv;
	private MyListViewItemClickListener mCallbakListViewItemClick;

	public MyDrawerLeftFragment() {
		// system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyDrawerLeftFragment();
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		if(activity instanceof MyDrawerLeftFragment.MyListViewItemClickListener){
			mCallbakListViewItemClick = (MyDrawerLeftFragment.MyListViewItemClickListener)activity;
		}else{
			this.mCallbakListViewItemClick = new MyDrawerLeftFragment.MyListViewItemClickListener() {
				
				@Override
				public void connectableItemPosition(int position) {
					// do nothing
					
				}
			};
		}
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_leftdrawer, container, false);
		mlv = (ListView) v.findViewById(R.id.ld_listview);
		mlv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		mlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mlv.setItemChecked(arg2, true);
				mCallbakListViewItemClick.connectableItemPosition(arg2);
				
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.mlv.setAdapter(new ArrayAdapter<String>(
				this.getActivity(),
				R.layout.row_leftdrawerlist,
				this.getResources().getStringArray(R.array.titlesArray)));
		if(savedInstanceState == null){
			this.mlv.setItemChecked(0, true);
		}
		super.onActivityCreated(savedInstanceState);
	}
}
