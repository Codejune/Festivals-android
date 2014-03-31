package com.festivals;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
	public interface Delegate {
		public void success(String path,Bitmap d);

		public void fail();
	}

	private Delegate _d;
	public FestivalApplication app;
	private String path;

	String url;

	public ImageDownloadTask(FestivalApplication app, Delegate _d) {
		super();
		this._d = _d;
		this.app = app;
	}

	private Bitmap LoadImageFromWebOperations(String url) {
		this.url = url;
		try {
			String downDir = "/sdcard" + "/festivals/";
			Log.e("aaa", downDir);
			new File(downDir).mkdirs();
			String[] aa = url.split("/");

			if (new File(downDir + aa[aa.length - 1].replace("?type=m1501","")).isFile()) {
				return BitmapFactory.decodeFile(downDir + aa[aa.length - 1].replace("?type=m1501",""));
			}
			FileUrlDownload.fileUrlDownload(url, downDir);
			path = downDir + aa[aa.length - 1].replace("?type=m1501","");
			return BitmapFactory.decodeFile(downDir + aa[aa.length - 1].replace("?type=m1501",""));
		} catch (Exception e) {

			Log.e("ImageDownloadTask", "", e);
			return null;
		}
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		final String url = params[0];
		return LoadImageFromWebOperations(url);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (null != result) {
			_d.success(path,result);
		}

	}

}