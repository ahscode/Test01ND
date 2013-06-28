package ahscode.test01nd;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
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
	protected float mDrawerLayoutTouch_DownX;
	protected float mRightDrawerTouch_DownX;
	protected float mRightDrawerTouch_UpX;
	private float mContentTouch_DownX;
	private float mContentTouch_UpX;
	private float mContentTouch_DownY;
	private float mContentTouch_UpY;
	private int mDiplayWidth;
	private int mDiplayHeight;
	private int mContentsMonitorHeight;
	private int mContentsMonitorWidth;
	private int mResultExculedBarsHeight;

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
			//TODO control lockmode opening and closing
			@Override
			public void onDrawerClosed(View drawerView) {
				if(drawerView == mAreaDrawer_Left){
					invalidateOptionsMenu();
					mCallbackLeftDrawerOpenListener.conactableLeftDrawer(mDrawerLayout.isDrawerOpen(drawerView));
				}
				if(drawerView == mAreaDrawer_Right){
					mDrawerLayout.setOnTouchListener(null);
					mAreaDrawer_Right.setOnTouchListener(null);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mAreaDrawer_Right);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mAreaDrawer_Left);
				}
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		this.mContentsMonitorHeight = this.mDrawerLayout.getHeight();//content size 1038
		this.mContentsMonitorWidth = this.mDrawerLayout.getWidth();//content size 720
		Log.d("contentsXY", ""+this.mContentsMonitorWidth+"/"+this.mContentsMonitorHeight);
		Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        this.mDiplayWidth = p.x;
        this.mDiplayHeight = p.y;
        Log.d("DisplayXY", ""+this.mDiplayWidth+"/"+this.mDiplayHeight);
        this.mResultExculedBarsHeight = this.mDiplayHeight - this.mContentsMonitorHeight;//TODO  Actionbar and StatusBar
		super.onWindowFocusChanged(hasFocus);
	}

	//最上位レイヤータッチイベント
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Right)){
			return super.dispatchTouchEvent(ev);
		}
		this.setRightDrawerTouchEvent();
		this.setDrawerLayoutTouch();
		int action = ev.getAction();
		switch(action){
		case KeyEvent.ACTION_DOWN:
			this.mContentTouch_DownX = ev.getX();
			this.mContentTouch_DownY = ev.getY();
			break;
		case KeyEvent.ACTION_UP:
			this.mContentTouch_UpX = ev.getX();
			this.mContentTouch_UpY = ev.getY();
			if((this.mContentTouch_DownY > this.mResultExculedBarsHeight) && (this.mContentTouch_UpY > this.mResultExculedBarsHeight)){
				if(this.mContentTouch_UpX - this.mContentTouch_DownX> 100){
					this.mDrawerLayout.closeDrawer(mAreaDrawer_Right);
					return true;
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void setRightDrawerTouchEvent() {
		mAreaDrawer_Right.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;// TODO anytime return true  if you want to complete touchevent in this layer
			}
		});
		
	}

	private void setDrawerLayoutTouch() {

		mDrawerLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action){
				case KeyEvent.ACTION_UP:
					mDrawerLayoutTouch_DownX = event.getX();
					Log.d("drawerLayout_touch_up", "x"+mDrawerLayoutTouch_DownX);
					mDrawerLayout.closeDrawer(mAreaDrawer_Right);
					return true;// TODO  touched area out of rightdrawer  it's mean that close rightdrawer so return true 
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


	//TODO Use BackKey it'mean that rightdrawer close
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				if(this.mDrawerLayout.isDrawerOpen(mAreaDrawer_Right)){
					this.mDrawerLayout.closeDrawer(mAreaDrawer_Right);
					return true;
				}
			};
		}
		return super.dispatchKeyEvent(event);//system use
	}

	/*callback-MyDrawerLeftFragment.MyListViewItemClickListener*/
	@Override
	public void connectableItemPosition(int position) {
		this.mDrawerLayout.closeDrawer(mAreaDrawer_Left);
	}
}
