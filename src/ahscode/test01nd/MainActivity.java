package ahscode.test01nd;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity 
implements MyLeftDrawerFragment.MyListViewItemEventListener{

	interface MyDrawerEventListener{
		void changeLeftDrawerOpenFlg(boolean leftDrawerOpenflg);
		void changeRightDrawerOpenFlg(boolean rightDrawerOpenflg);
	}

	private String mTitle;
	private String mTag_fragment_leftdrawer;
	private String mTag_fragment_rightdrawer;
	private String[] mTitlesArray;
	private String mBundleKey_selectedNumber;
	private String mBundleKey_selectedTytle;
	private DrawerLayout mDrawerLayout;
	private FrameLayout mArea_LeftDrawer;
	private FrameLayout mArea_RightDrawer;
	private FrameLayout mArea_Contents;
	private String mOpened_Tytle;
	private int mOpen_Num;
	private MyDrawerEventListener mDrawerCallback;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	protected boolean mLeftDrawerOpenFlg;
	protected boolean mRightDrawerOpenFlg;
	private String mBundleKey_leftDrawerOpneFlg;
	private String mBundleKey_rightDrawerOpneFlg;
	private int mContentsMonitorHeight;
	private int mDiplayHeight;
	private int mResultExculedBarsHeight;
	private float mContentTouch_DownX;
	private float mContentTouch_DownY;
	private float mContentTouch_UpX;
	private float mContentTouch_UpY;
	protected float mDrawerLayoutTouch_DownX;
	private final int  mMoveRange = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getActionBar().setHomeButtonEnabled(true);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setParams(savedInstanceState);//レイアウト以外のメンバ変数の初期値を設定
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//call before setContentView
		this.setLayout();//レイアウトをメンバ変数として設定

		mActionBarDrawerToggle = this.setActionBarToggle();//toggleの作成
		this.mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

		Fragment leftdrawerFragment = this.getFragmentManager().findFragmentByTag(mTag_fragment_leftdrawer);
		leftdrawerFragment = this.setLeftDrawerFragment(leftdrawerFragment,savedInstanceState);
		Fragment contentsFragment = this.setContentsFragment(savedInstanceState);
		Fragment rightdrawerFragment = this.getFragmentManager().findFragmentByTag(mTag_fragment_rightdrawer);
		rightdrawerFragment = this.setRightDrawerFragment(rightdrawerFragment);
		//Fragmentのインスタンスだけ生成してあとからまとめてコミット
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		if(!leftdrawerFragment.isAdded()){
			ft = ft.replace(mArea_LeftDrawer.getId(), leftdrawerFragment, mTag_fragment_leftdrawer);
		}
		if(!contentsFragment.isAdded()){
			ft = ft.replace(mArea_Contents.getId(), contentsFragment, mOpened_Tytle);
		}
		if(!rightdrawerFragment.isAdded()){
			ft = ft.replace(mArea_RightDrawer.getId(), rightdrawerFragment, mTag_fragment_rightdrawer);
		}
		ft.commit();
		
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				if(this.mDrawerLayout.isDrawerOpen(mArea_RightDrawer)){
					this.mDrawerLayout.closeDrawer(mArea_RightDrawer);
					return true;
				}
			};
		}
		return super.dispatchKeyEvent(event);//system use
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		this.mContentsMonitorHeight = this.mDrawerLayout.getHeight();//content size 1038
		this.mDrawerLayout.getWidth();
		Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        this.mDiplayHeight = p.y;
        this.mResultExculedBarsHeight = this.mDiplayHeight - this.mContentsMonitorHeight;//TODO  Actionbar and StatusBar
        super.onWindowFocusChanged(hasFocus);
	}

	//上位レイヤータッチイベント
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!this.mDrawerLayout.isDrawerOpen(mArea_RightDrawer)){
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
				if(this.mContentTouch_UpX - this.mContentTouch_DownX> mMoveRange){
					this.mDrawerLayout.closeDrawer(mArea_RightDrawer);
					return true;
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void setRightDrawerTouchEvent() {
		mArea_RightDrawer.setOnTouchListener(new OnTouchListener() {

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
					mDrawerLayout.closeDrawer(mArea_RightDrawer);
					return true;// TODO  touched area out of rightdrawer  it's mean that close rightdrawer so return true 
				}
				return false;
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			if(this.mDrawerLayout.isDrawerOpen(mArea_RightDrawer)){
				this.mDrawerLayout.closeDrawer(mArea_RightDrawer);
			}
			this.mActionBarDrawerToggle.onOptionsItemSelected(item);/*use toggle final*/
		}else{
			if(this.mDrawerLayout.isDrawerOpen(mArea_LeftDrawer)){
				Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	private ActionBarDrawerToggle setActionBarToggle() {
		mActionBarDrawerToggle = new ActionBarDrawerToggle(
				this, mDrawerLayout, R.drawable.ic_drawer, 0, 0){

			@Override
			public void onDrawerClosed(View drawerView) {
				if(drawerView == mArea_LeftDrawer){
					mLeftDrawerOpenFlg = false;
					mDrawerCallback.changeLeftDrawerOpenFlg(mLeftDrawerOpenFlg);
				}
				if(drawerView == mArea_RightDrawer){
					mRightDrawerOpenFlg = false;
					mDrawerCallback.changeRightDrawerOpenFlg(mRightDrawerOpenFlg);
					mDrawerLayout.setOnTouchListener(null);
					mArea_RightDrawer.setOnTouchListener(null);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mArea_RightDrawer);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mArea_LeftDrawer);
				}
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if(drawerView == mArea_LeftDrawer){
					mLeftDrawerOpenFlg = true;
					mDrawerCallback.changeLeftDrawerOpenFlg(mLeftDrawerOpenFlg);
				}
				if(drawerView == mArea_RightDrawer){
					mRightDrawerOpenFlg = true;
					mDrawerCallback.changeRightDrawerOpenFlg(mRightDrawerOpenFlg);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mArea_LeftDrawer);
					mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, mArea_RightDrawer);
				}
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);//call first!
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mDrawerLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			
		};
		return mActionBarDrawerToggle;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu_layout_activity, menu);
		this.getActionBar().setTitle(mTitle);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//TODO flgがtrueのときは表示する。falseのときは非表示にする。
		if(mLeftDrawerOpenFlg== true){
			menu.setGroupVisible(R.id.menu_group_mainactivity, true);
			this.getActionBar().setTitle(this.mTitle);
		}else{
			menu.setGroupVisible(R.id.menu_group_mainactivity, false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState == null){
			outState = new Bundle();
		}
		outState.putInt(mBundleKey_selectedNumber, mOpen_Num);
		outState.putString(mBundleKey_selectedTytle, mOpened_Tytle);
		outState.putBoolean(mBundleKey_leftDrawerOpneFlg, mLeftDrawerOpenFlg);
		outState.putBoolean(mBundleKey_rightDrawerOpneFlg, mRightDrawerOpenFlg);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState ==null){
			return;
		}
		mOpen_Num = savedInstanceState.getInt(mBundleKey_selectedNumber);
		mOpened_Tytle = savedInstanceState.getString(mBundleKey_selectedTytle);
		mLeftDrawerOpenFlg = savedInstanceState.getBoolean(mBundleKey_leftDrawerOpneFlg);
		mRightDrawerOpenFlg = savedInstanceState.getBoolean(mBundleKey_rightDrawerOpneFlg);
	}

	private Fragment setRightDrawerFragment(Fragment rightdrawerFragment) {
		if(rightdrawerFragment == null){
			rightdrawerFragment = MyRightDrawerFragment.newInstance();
		}
		return rightdrawerFragment;
	}

	private Fragment setLeftDrawerFragment(Fragment leftdrawerFragment, Bundle savedInstanceState) {
		if(leftdrawerFragment == null){
			leftdrawerFragment = MyLeftDrawerFragment.newInstance();
			if(savedInstanceState == null){
				Bundle b = new Bundle();
				b.putInt(mBundleKey_selectedNumber, mOpen_Num);
				b.putString(mBundleKey_selectedTytle, mOpened_Tytle);
				leftdrawerFragment.setArguments(b);
			}else{
				mOpen_Num = savedInstanceState.getInt(mBundleKey_selectedNumber);
				mOpened_Tytle = savedInstanceState.getString(mBundleKey_selectedTytle);
				leftdrawerFragment.setArguments(savedInstanceState);
			}
		}
		return leftdrawerFragment;
	}

	private void setLayout() {
		this.setContentView(R.layout.layout_drawerlayout);
		mArea_LeftDrawer = (FrameLayout)this.findViewById(R.id.FlameLayout_UseLeftdrawer);
		mArea_RightDrawer = (FrameLayout)this.findViewById(R.id.FlameLayout_UseRightdrawer);
		mArea_Contents = (FrameLayout)this.findViewById(R.id.FlameLayout_UseContents);
		mDrawerLayout = (DrawerLayout)this.findViewById(R.id.body_DrawerLayout);
	}

	private void setParams(Bundle savedInstanceState) {
		mTitle = this.getResources().getString(R.string.app_name);
		mTag_fragment_leftdrawer = this.getResources().getString(R.string.tag_leftdrawr_fragment);
		mTag_fragment_rightdrawer = this.getResources().getString(R.string.tag_rightdrawr_fragment);
		mTitlesArray = this.getResources().getStringArray(R.array.titlesArray);
		mBundleKey_selectedNumber = this.getResources().getString(R.string.selected_number);
		mBundleKey_selectedTytle = this.getResources().getString(R.string.selected_tytle);
		mBundleKey_leftDrawerOpneFlg = this.getResources().getString(R.string.leftdrawer_openFlg);
		mBundleKey_rightDrawerOpneFlg = this.getResources().getString(R.string.rightdrawer_openFlg);
		if(savedInstanceState == null){
			mOpen_Num = 0;
			mOpened_Tytle = mTitlesArray[mOpen_Num];
			mLeftDrawerOpenFlg = false;
			mRightDrawerOpenFlg = false;
		}else{
			mOpen_Num = savedInstanceState.getInt(mBundleKey_selectedNumber);
			mOpened_Tytle = savedInstanceState.getString(mBundleKey_selectedTytle);
			mLeftDrawerOpenFlg = savedInstanceState.getBoolean(mBundleKey_leftDrawerOpneFlg);
			mRightDrawerOpenFlg = savedInstanceState.getBoolean(mBundleKey_rightDrawerOpneFlg);
		}
	}

	//callback-MyLeftDrawerFragment.MyListViewItemEventListener
	@Override
	public void connectItemClickEvent(Bundle bundle) {
		mLeftDrawerOpenFlg = false;
		this.invalidateOptionsMenu();
		Fragment f = this.setContentsFragment(bundle);
		if(!f.isAdded()){
			this.getFragmentManager().beginTransaction()
			.replace(mArea_Contents.getId(), f,mOpened_Tytle)
			.commit();
		}
		mDrawerLayout.closeDrawer(mArea_LeftDrawer);
	}

	private Fragment setContentsFragment(Bundle bundle) {
		if(bundle == null){
			bundle = new Bundle();
			bundle.putInt(mBundleKey_selectedNumber, mOpen_Num);
			bundle.putString(mBundleKey_selectedTytle, mOpened_Tytle);
		}else{
			mOpen_Num = bundle.getInt(mBundleKey_selectedNumber);
			mOpened_Tytle = bundle.getString(mBundleKey_selectedTytle);
		}
		Fragment f = this.getFragmentManager().findFragmentByTag(mOpened_Tytle);
		if(f == null){
			switch(mOpen_Num){
			case 0:	f = MyTodayFragment.newInstance();break;
			case 1:f = MySettingFragment.newInstance();break;
			default:new Exception("swithcの中に値が存在しません");
			}
			f.setArguments(bundle);
		}
		if(f instanceof MainActivity.MyDrawerEventListener){
			mDrawerCallback = (MainActivity.MyDrawerEventListener)f;
		}else{
			mDrawerCallback = new MyDrawerEventListener() {

				@Override
				public void changeLeftDrawerOpenFlg(boolean flg) {
					//do nothing
				}

				@Override
				public void changeRightDrawerOpenFlg(boolean rightDrawerOpenflg) {
					//do nothing
				}
			};
		}
		return f;
	}


}
