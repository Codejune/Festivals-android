package com.festivals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

public class CorneredImageView extends ImageView {

	public CorneredImageView(Context context) {
		super(context);
	}

	public CorneredImageView(Context context, AttributeSet set) {
		super(context, set);
	}

	public CorneredImageView(Context context, AttributeSet set, int i) {
		super(context, set, i);
	}

	public void setImageBitmap(String path, final Bitmap bm2) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap bm = bm2;
					bm = Bitmap.createScaledBitmap(bm, getWidth() ,
							getMeasuredHeight(), false);
					bm = Util.apply(getContext(), bm, 25);
					final Bitmap bm3 = bm;
					CorneredImageView.this.post(new Runnable() {
						@Override
						public void run() {
							final float radius = TypedValue.applyDimension(
									TypedValue.COMPLEX_UNIT_DIP, 20,
									getResources().getDisplayMetrics());
							CorneredImageView.this
									.setImageBitmap(new RoundedDrawable(bm3)
											.setBorderColor(Color.BLACK)
											.setBorderWidth(0)
											.setCornerRadius(radius).setOval(false)
											.toBitmap());

						};
					});

				}
			}).start();
		} catch (Exception e) {

		}
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		return bitmap;
	}

	public String onlyName(String sFileName) {

		int FileIdx = sFileName.lastIndexOf(".");
		return sFileName.substring(0, FileIdx);
	}

}
