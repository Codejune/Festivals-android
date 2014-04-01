package com.festivals;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	// Recommened Charset UTF-8
		private String encoding = "UTF-8";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	VerticalViewPager VPager;
	static OnResponse o1;
	static OnResponse o2;
	static OnResponse o3;
	static OnResponse o4;
	static LinearLayout v1;
	static LinearLayout v2;
	static LinearLayout v3;
	static LinearLayout v4;
	static int location = 1;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (!isNetworkConnected(this)) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("네트워크 연결 오류")
					.setMessage("네트워크 연결 상태 확인 후 다시 시도해 주십시요.")
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
		}
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);
		VPager = (VerticalViewPager) findViewById(R.id.vpager);
		final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		VPager.setOverScrollMode(VPager.OVER_SCROLL_NEVER);
		VPager.setAdapter(new SAdapter(getSupportFragmentManager()));
		VPager.setOffscreenPageLimit(9);
		VPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				location = arg0;
				try {
					if (v1 != null)
						v1.removeAllViewsInLayout();
					if (v2 != null)
						v2.removeAllViewsInLayout();
					if (v3 != null)
						v3.removeAllViewsInLayout();
					if (v4 != null)
						v4.removeAllViewsInLayout();
					if (location != 8) {
						Util.getFestivals(1, 1, arg0 + 1, o1);
						Util.getFestivals(2, 1, arg0 + 1, o2);
						Util.getFestivals(3, 1, arg0 + 1, o3);
						Util.getFestivals(4, 1, arg0 + 1, o4);
					} else {
						Util.getFestivalsSeason(1, 1, o1);
						Util.getFestivalsSeason(2, 1, o2);
						Util.getFestivalsSeason(3, 1, o3);
						Util.getFestivalsSeason(4, 1, o4);
					}
				} catch (Exception e) {

				}
			}
		});

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new FestivalFragment();
			Bundle args = new Bundle();
			args.putInt(FestivalFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

	}

	public class SAdapter extends FragmentPagerAdapter {

		public SAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new LayoutFragment();
			Bundle args = new Bundle();
			args.putInt(LayoutFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 9;
		}

	}

	public class LogoAdapter extends FragmentPagerAdapter {

		public LogoAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new LayoutFragment();
			Bundle args = new Bundle();
			args.putInt(LayoutFragment.layout, position + 1);

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 8;
		}

	}

	public static class LayoutFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";
		int page;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String layout = "layoutid";

		public LayoutFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.si_seoul, container,
					false);
			int page = getArguments().getInt(ARG_SECTION_NUMBER);
			ImageView icon = (ImageView) rootView.findViewById(R.id.imageView1);
			TextView name = (TextView) rootView.findViewById(R.id.textView1);
			switch (page) {
			case 1:
				icon.setImageResource(R.drawable.si_seoul_icon);
				name.setText(R.string.si_seoul);
				break;
			case 2:
				icon.setImageResource(R.drawable.si_incheon_icon);
				name.setText(R.string.si_incheon);
				break;
			case 3:
				icon.setImageResource(R.drawable.si_daejeon_icon);
				name.setText(R.string.si_daejeon);
				break;
			case 4:
				icon.setImageResource(R.drawable.si_daegu_icon);
				name.setText(R.string.si_daegu);
				break;
			case 5:
				icon.setImageResource(R.drawable.si_ulsan_icon);
				name.setText(R.string.si_ulsan);
				break;
			case 6:
				icon.setImageResource(R.drawable.si_gwangju_icon);
				name.setText(R.string.si_gwangju);
				break;
			case 7:
				icon.setImageResource(R.drawable.si_busan_icon);
				name.setText(R.string.si_busan);
				break;
			case 8:
				icon.setImageResource(R.drawable.do_gyeonggi_icon);
				name.setText(R.string.do_gyeonggi);
				break;
			case 9:
				icon.setImageResource(R.drawable.korea_icon);
				name.setText("전국");
			}
			return rootView;

		}
		/**
		 * A dummy fragment representing a section of the app, but that simply
		 * displays dummy text.
		 * 
		 * 
		 */

	}

	public static class FestivalFragment extends Fragment implements OnResponse {
		ScrollView sl_festivals;
		LinearLayout ll_content;
		int page;
		int contentpage = 1;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public FestivalFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			page = getArguments().getInt(ARG_SECTION_NUMBER);
			sl_festivals = (ScrollView) rootView
					.findViewById(R.id.lv_festivals);
			sl_festivals.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
			/*
			 * sl_festivals.setOnTouchListener(new OnTouchListener() {
			 * 
			 * @Override public boolean onTouch(View arg0, MotionEvent arg1) {
			 * if(sl_festivals.getHeight() < sl_festivals.getScrollY()) {
			 * if(arg1.getAction() == MotionEvent.ACTION_UP)
			 * Util.getFestivals(page, contentpage++, location,
			 * FestivalFragment.this); } Log.e("page",contentpage+""); return
			 * false; } });
			 */
			ll_content = (LinearLayout) rootView.findViewById(R.id.ll_content);
			switch (page) {
			case 1:
				rootView.findViewById(R.id.container).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.background));
				o1 = this;
				v1 = ll_content;
				break;
			case 2:
				rootView.findViewById(R.id.container).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.summer2));
				o2 = this;
				v2 = ll_content;
				break;
			case 3:
				rootView.findViewById(R.id.container).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.fall3));
				o3 = this;
				v3 = ll_content;
				break;
			case 4:
				rootView.findViewById(R.id.container).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.winter2));
				o4 = this;
				v4 = ll_content;
				break;
			}

			Util.getFestivals(page, 1, location, this);
			return rootView;
		}

		@Override
		public void onResponse(ArrayList<Festival> mFestivals) {

			FestivalAdapter adapter = new FestivalAdapter(getActivity(),
					mFestivals, page);
			for (int i = 0; i < adapter.getCount(); i++) {
				ll_content.addView(adapter.getView(i, null, null));
			}

		}
	}

	public boolean isNetworkConnected(Context context) {
		boolean isConnected = false;

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobile.isConnected() || wifi.isConnected()) {
			isConnected = true;
		} else {
			isConnected = false;
		}
		return isConnected;
	}

	// 하드웨어 뒤로가기버튼 이벤트 설정.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		// 하드웨어 뒤로가기 버튼에 따른 이벤트 설정
		case KeyEvent.KEYCODE_BACK:

			new AlertDialog.Builder(this)
					.setTitle("종료")
					.setMessage("어플리케이션을 종료합니다")
					.setPositiveButton("예",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 프로세스 종료.
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							}).setNegativeButton("아니오", null).show();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
