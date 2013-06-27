package ahscode.test01nd;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity
implements MyDrawerLeftFragment.MyListViewItemClickListener{
	interface LeftDrawerStateListener{
		void conactableLeftDrawer(boolean flg);
	}
	private DrawerLayout mDrawerLayout;
	private FrameLayout mAreaDrawer_Left;
	private FrameLayout mAreaDrawer_Right;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private LeftDrawerStateListener mCallbackLeftDrawerOpenListener;
	private final float mRange = 100f;//rightdrawer is use for action-closing
	private float mDownPointX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getActionBar().setHomeButtonEnabled(true);// use toggle true
		this.getActionBar().setDisplayHomeAsUpEnabled(true);//use toggle. next onPostCreate
		setContentView(R.layout.layout_drawerlayout);
		mDrawerLayout = (DrawerLayout) this.findViewById(R.id.body_DrawerLayout);
		mAreaDrawer_Left = (FrameLayout)this.findViewById(R.id.FlameLayout_UseLeftdrawer);
		mAreaDrawer_Right = (FrameLayout)this.findViewById(R.id.FlameLayout_UseRightdrawer);


		this.setSomeFragments(savedInstanceState);

		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, 0, 0){
			@Override
			public void onDrawerClosed(View drawerView) {
				if(drawerView == mAreaDrawer_Left){
					invalidateOptionsMenu();
					mCallbackLeftDrawerOpenListener.conactableLeftDrawer(mDrawerLayout.isDrawerOpen(drawerView));
				}
				if(drawerView == mAreaDrawer_Right){
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mAreaDrawer_Right);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mAreaDrawer_Left);
				}
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				//TODO dialog表示状態ならダイアログフラグメントのほうから制御。
				//activityのシングルタスクのみこいつは効くはずだからそれは検証しない。
				if(drawerView == mAreaDrawer_Left){
					mCallbackLeftDrawerOpenListener.conactableLeftDrawer(mDrawerLayout.isDrawerOpen(drawerView));
					invalidateOptionsMenu();
				}
				if(drawerView == mAreaDrawer_Right){
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mAreaDrawer_Left);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, mAreaDrawer_Right);
				}
			}
		};
		this.mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
		this.mDrawerLayout.setOnTouchListener(null);
		mDrawerLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!mDrawerLayout.isDrawerOpen(mAreaDrawer_Right)){
					return false;
				}
				int action = event.getAction();
				switch(action){
				case KeyEvent.ACTION_DOWN:
					mDownPointX = event.getX();
					return true;
				case KeyEvent.ACTION_UP:
					int uppointX = (int) event.getX();
					int[] area_rightdrawerXY = new int[2];
					Log.e("up", "uppointX"+uppointX+"mDownPointX"+mDownPointX+"area_rightdrawerXY[0]"+area_rightdrawerXY[0]);
					mAreaDrawer_Right.getLocationInWindow(area_rightdrawerXY);
					if(mDownPointX< area_rightdrawerXY[0]){
						mDrawerLayout.closeDrawer(mAreaDrawer_Right);
						return true;
					}else{
						if(uppointX - mDownPointX > mRange){
							mDrawerLayout.closeDrawer(mAreaDrawer_Right);
							return true;
						}
					}
				}
				return false;
			}
		});

	}

	/*use toggle.Next configrationchanged */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		this.mActionBarDrawerToggle.syncState();
		super.onPostCreate(savedInstanceState);
	}

	/*use toggle. next onoptionsitemselected*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		this.mActionBarDrawerToggle.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu_layout_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			if(this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Right)){
				this.mDrawerLayout.closeDrawer(mAreaDrawer_Right);
			}
			this.mActionBarDrawerToggle.onOptionsItemSelected(item);/*use toggle final*/
		}else{
			if(this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Left)){
				Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Left) == true){
			menu.setGroupVisible(R.id.menu_group_mainactivity, true);
			this.getActionBar().setTitle(this.getResources().getString(R.string.app_name));
		}else{
			menu.setGroupVisible(R.id.menu_group_mainactivity, false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	private void setSomeFragments(Bundle savedInstanceState) {
		String tag_leftdrawrfragment = this.getResources().getString(R.string.tag_leftdrawr_fragment);
		String tag_rightdrawrfragment = this.getResources().getString(R.string.tag_rightdrawr_fragment);
		String[] titlesArray = this.getResources().getStringArray(R.array.titlesArray);
		if(savedInstanceState == null){
			int default_number = 0;
			Fragment f_left = MyDrawerLeftFragment.newInstance();
			Bundle b = new Bundle();
			b.putInt(this.getResources().getString(R.string.selected_number), default_number);
			f_left.setArguments(b);
			Fragment f_right = MyDrawerRightFragment.newInstance();
			Fragment f_contents = MyContents00Fragment.newInstance();
			if(f_contents instanceof LeftDrawerStateListener){
				mCallbackLeftDrawerOpenListener = (LeftDrawerStateListener)f_contents;
			}else{
				this.mCallbackLeftDrawerOpenListener = new LeftDrawerStateListener() {

					@Override
					public void conactableLeftDrawer(boolean flg) {
						// do nothing

					}
				};
			}
			this.getFragmentManager().beginTransaction()
			.replace(R.id.FlameLayout_UseLeftdrawer, f_left, tag_leftdrawrfragment)
			.replace(R.id.FlameLayout_UseRightdrawer, f_right, tag_rightdrawrfragment)
			.replace(R.id.FlameLayout_UseContents, f_contents, titlesArray[default_number])
			.commit();
		}else{
			Fragment f_left = this.getFragmentManager().findFragmentByTag(tag_leftdrawrfragment);
			if(f_left == null){
				f_left = MyDrawerLeftFragment.newInstance();
			}
			if(!(f_left.isAdded())){
				f_left.setArguments(savedInstanceState);
				this.getFragmentManager().beginTransaction()
				.replace(R.id.FlameLayout_UseLeftdrawer, f_left, tag_leftdrawrfragment)
				.commit();
			}
			Fragment f_right = this.getFragmentManager().findFragmentByTag(tag_rightdrawrfragment);
			if(f_right == null){
				f_right = MyDrawerRightFragment.newInstance();
			}
			if(!(f_right.isAdded())){
				this.getFragmentManager().beginTransaction()
				.replace(R.id.FlameLayout_UseRightdrawer, f_right, tag_rightdrawrfragment)
				.commit();
			}
			Fragment f_contents = this.getFragmentManager().findFragmentByTag(
					titlesArray[savedInstanceState.getInt(this.getResources().getString(R.string.selected_number))]);
			if(f_contents == null){
				f_contents = MyContents00Fragment.newInstance();
			}
			if(!(f_contents.isAdded())){
				this.getFragmentManager().beginTransaction()
				.replace(R.id.FlameLayout_UseContents, f_contents, this.getResources().getString(R.string.selected_number))
				.commit();
			}
			if(f_contents instanceof LeftDrawerStateListener){
				mCallbackLeftDrawerOpenListener = (LeftDrawerStateListener)f_contents;
			}else{
				this.mCallbackLeftDrawerOpenListener = new LeftDrawerStateListener() {

					@Override
					public void conactableLeftDrawer(boolean flg) {
						// do nothing

					}
				};
			}
		}

	}


	//rightdrawer close
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				Log.d("KeyCode","KeyCode:"+ event.getKeyCode());
				if(this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Right)){
					this.mDrawerLayout.closeDrawer(mAreaDrawer_Right);
					return true;
				}
			};
		}
		return super.dispatchKeyEvent(event);
	}

	/*callback-MyDrawerLeftFragment.MyListViewItemClickListener*/
	@Override
	public void connectableItemPosition(int position) {
		this.mDrawerLayout.closeDrawer(mAreaDrawer_Left);

	}
}
