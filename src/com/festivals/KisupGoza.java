package com.festivals;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class KisupGoza extends ImageButton {
	public KisupGoza(Context context) {
		super(context);
	}

	public KisupGoza(Context context, AttributeSet set) {
		super(context, set);
	}

	public KisupGoza(Context context, AttributeSet set, int i) {
		super(context, set, i);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);
		setColorFilter(new LightingColorFilter(0x000000, 0xffffff));
		setBackgroundDrawable(null);
	}

}
