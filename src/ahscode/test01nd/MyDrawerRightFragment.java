package ahscode.test01nd;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MyDrawerRightFragment extends Fragment {

	public MyDrawerRightFragment() {
		// system use
	}

	public static Fragment newInstance() {
		Fragment f = new MyDrawerRightFragment();
		return f;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_rightdrawer, container, false);
		Button b = (Button) v.findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "テスト", Toast.LENGTH_SHORT).show();
				
			}
		});
		return v;
	}
}
