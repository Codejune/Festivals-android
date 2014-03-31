package com.festivals;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.festivals.ImageDownloadTask.Delegate;

public class FestivalAdapter extends BaseAdapter {
	ArrayList<Festival> mFestivals;
	Context context;
	LayoutInflater inflater;
	FestivalApplication app;
	int page;

	public FestivalAdapter(Context context, ArrayList<Festival> mFestivals,
			int page) {
		// TODO Auto-generated constructor stub
		this.mFestivals = mFestivals;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		app = (FestivalApplication) context.getApplicationContext();
		this.page = page;

	}

	public int getCount() {
		return mFestivals.size();
	}

	@Override
	public Festival getItem(int position) {
		return mFestivals.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Festival festival = getItem(position);
		TextView tv_festival = null;
		TextView tv_date = null;
		ImageView iv = null;
		ImageView iv_bg = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.festivals_list_item, null);
			tv_festival = (TextView) convertView.findViewById(R.id.name);
			tv_date = (TextView) convertView.findViewById(R.id.day);
			iv = (ImageView) convertView.findViewById(R.id.thumbnail);
			iv_bg = (ImageView) convertView.findViewById(R.id.iv_bg);
			tv_festival.setFocusable(true);
			tv_festival.setEllipsize(TruncateAt.MARQUEE);
			tv_festival.setSelected(true);
			Holder holder = new Holder();
			holder.tv1 = tv_festival;
			holder.tv2 = tv_date;
			holder.iv1 = iv;
			holder.iv2 = iv_bg;
			convertView.setTag(holder);
		} else {
			Holder holder = (Holder) convertView.getTag();
			tv_festival = holder.tv1;
			tv_date = holder.tv2;
			iv = holder.iv1;
			iv_bg = holder.iv2;
		}
		tv_date.setText(getItem(position).date);
		tv_festival.setText(getItem(position).name);

		iv_bg.setColorFilter(new LightingColorFilter(0xFF999999, 0x00000000));
		final ImageView fiv = iv;
		final ImageView fiv_bg = iv_bg;
		final View see = convertView.findViewById(R.id.see);
		new ImageDownloadTask(app, new Delegate() {
			@Override
			public void success(final String path,final Bitmap d) {
				fiv.setImageBitmap(d);
				((CorneredImageView)fiv_bg).setImageBitmap(path,d);
				OnClickListener listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch(page) {
						case 1:
							Util.makeDialog(context, context.getResources()
									.getDrawable(R.drawable.background), d,
									festival);
							break;
						case 2:
							Util.makeDialog(context, context.getResources()
									.getDrawable(R.drawable.summer2), d,
									festival);
							break;
						case 3:
							Util.makeDialog(context, context.getResources()
									.getDrawable(R.drawable.fall3), d,
									festival);
							break;
						case 4:
							Util.makeDialog(context, context.getResources()
									.getDrawable(R.drawable.winter2), d,
									festival);
							break;
						}


					}
				};
				fiv_bg.setOnClickListener(listener);
				see.setOnClickListener(listener);

			}

			@Override
			public void fail() {
				// TODO Auto-generated method stub

			}
		}).execute(getItem(position).img);
		return convertView;
	}

	public static class Holder {
		TextView tv1;
		TextView tv2;
		ImageView iv1;
		ImageView iv2;
	}

}
