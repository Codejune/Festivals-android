package com.festivals;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.Toast;

public class FestivalApplication extends Application {
	private LruCache<String, Bitmap> mMemoryCache;
	Handler handler = new Handler();
	public FestivalApplication() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

}
