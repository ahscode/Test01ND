package ahscode.test01nd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyLeftDrawerFragment extends Fragment {
	interface MyListViewItemEventListener{
		void connectItemClickEvent(Bundle bundle);
	}
	public static class ViewHolder {
		ImageView mIconImage;
		TextView mTytleText;

	}

	public static class MyArrayAdapter<T> extends ArrayAdapter<Contents> {

		private Context mContext;
		private List<Contents> mList;
		private ViewHolder mHolder;

		public MyArrayAdapter(Context context, int textViewResourceId, List<Contents> objects) {
			super(context, textViewResourceId, objects);
			mContext = context;
			mList = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				switch(position){
				case 0:case 1:
					v = inflater.inflate(R.layout.row_leftdrawer, parent, false);
					mHolder = new ViewHolder();
					mHolder.mIconImage = (ImageView) v.findViewById(R.id.ld_icon);
					mHolder.mTytleText = (TextView) v.findViewById(R.id.ld_title);
					break;
					default:new Exception("Adapterの設定に存在しない値です");
				}
				v.setTag(mHolder);
			}else{
				mHolder = (ViewHolder) v.getTag();
			}
			Contents item = mList.get(position);
			mHolder.mIconImage.setImageDrawable(item.mIconDrawable);
			mHolder.mTytleText.setText(item.mTytle);
			return v;
		}
		
	}

	public static class Contents {

		private Drawable mIconDrawable;
		private String mTytle;

		public void setItems(Drawable drawable, String string) {
			mIconDrawable = drawable;
			mTytle = string;
		}

	}

	private ListView mListView;
	private List<Contents> mContentsList;
	private MyArrayAdapter<Contents> mAdapter;
	private MyLeftDrawerFragment.MyListViewItemEventListener mItemEventCallback;

	public MyLeftDrawerFragment() {
		//system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyLeftDrawerFragment();
		f.setRetainInstance(true);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof MyLeftDrawerFragment.MyListViewItemEventListener){
			mItemEventCallback = (MyLeftDrawerFragment.MyListViewItemEventListener)activity;
		}else{
			mItemEventCallback = new MyLeftDrawerFragment.MyListViewItemEventListener() {
				
				@Override
				public void connectItemClickEvent(Bundle bundle) {
					//do nothing
				}
			};
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_leftdrawer, container, false);
		mListView = (ListView) v.findViewById(R.id.ld_listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Bundle b = getArguments();
				b.putInt(getResources().getString(R.string.selected_number), arg2);
				b.putString(getResources().getString(R.string.selected_tytle), mContentsList.get(arg2).mTytle);
				
				mItemEventCallback.connectItemClickEvent(b);
				mListView.setItemChecked(arg2, true);
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);//system use
		mContentsList = this.setContentsList();//リストをセット
		mAdapter = new MyArrayAdapter<Contents>(this.getActivity(), R.layout.row_leftdrawer, mContentsList);
		mListView.setAdapter(mAdapter);
		mListView.setItemChecked(
				getArguments().getInt(getResources().getString(R.string.selected_number))
				, true);
	}

	private List<Contents> setContentsList() {
		if(mContentsList == null){
			mContentsList = new ArrayList<Contents>();
		}
		mContentsList.clear();
		TypedArray iconDrawableArray = this.getResources().obtainTypedArray(R.array.titlesDrawableArray);
		String[] tytlesArray = this.getResources().getStringArray(R.array.titlesArray);
		for(int i = 0;i<tytlesArray.length;i++){
			Contents item = new Contents();
			item.setItems(iconDrawableArray.getDrawable(i),tytlesArray[i]);
			mContentsList.add(item);
		}
		iconDrawableArray.recycle();
		return mContentsList;
	}

}
