package ahscode.test01nd;

import ahscode.test01nd.MainActivity.LeftDrawerStateListener;
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

public class MyContents00Fragment extends Fragment  implements LeftDrawerStateListener{

	private EditText mEditText;
	private boolean mLeftDrawerOpenFlg;

	public MyContents00Fragment() {
		// system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyContents00Fragment();
		f.setRetainInstance(true);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_layout_c00, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() != android.R.id.home){
			if(this.mLeftDrawerOpenFlg != true){
				Toast.makeText(getActivity(), item.getTitle() +"MyContents" , Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if(this.mLeftDrawerOpenFlg == true){
			menu.setGroupVisible(R.id.menu_group_c00, false);
		}else{
			menu.setGroupVisible(R.id.menu_group_c00, true);
			this.getActivity().getActionBar().setTitle("title00");
		}
		
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_contents00, container, false);
		mEditText = (EditText) v.findViewById(R.id.c00_et00);
		Button btn = (Button) v.findViewById(R.id.c00_btn00);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), mEditText.getText(), Toast.LENGTH_SHORT).show();
			}
		});
		return v;
	}
	
	/* callback-MainActivity.LeftDrawerStateListener*/
	@Override
	public void conactableLeftDrawer(boolean flg) {
		mLeftDrawerOpenFlg = flg;
		this.getFragmentManager().invalidateOptionsMenu();
		
	}
	
}
