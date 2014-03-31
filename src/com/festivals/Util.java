package com.festivals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Util {

	private static final int CONNECTION_TIMEOUT = 5000;

	private static final String TYPE_CONTENT_JSON = "application/json";

	private static void getJson(final String url, final OnResponse res) {
		new GetFestivalsTask(res).execute(url);
	}

	public static void getFestivals(OnResponse res) {
		getJson("http://duk.so/festival/festivalInfo.php", res);
	}

	public static void getFestivals(final int season, final int page,
			final int place, OnResponse res) {
		getJson(String.format(
				"http://duk.so/festival/festivalInfo.php?season=%d&page=%d&place=%d",
				season, page, place), res);
	}

	public static void getFestivalsSeason(final int season, final int page,
			OnResponse res) {
		getJson(String.format(
				"http://duk.so/festival/festivalInfo.php?season=%d&page=%d",
				season, page), res);
	}

	public static void getFestivalsPlace(final int place, final int page,
			OnResponse res) {
		getJson(String
				.format("http://duk.so/festival/festivalInfo.php?place=%d",
						place, page),
				res);
	}

	private static class GetLocationTask extends AsyncTask<String, Void, Void> {

		String lat;
		String lng;

		Festival aa;

		public GetLocationTask(Festival aa) {
			this.aa = aa;
		}

		@Override
		protected void onPostExecute(Void result) {
			aa.lat = lat;
			aa.lng = lng;
			Log.e("Festivals", lat + "/" + lng);
		}

		protected Void doInBackground(String... addr) {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

			// prepare HttpPost
			String adr = addr[0];
			String url = null;
			try {
				Log.e("Festival", adr);
				url = "http://duk.so/festival/getAddress.php?address="
						+ URLEncoder.encode(adr, "UTF-8");
				Log.e("Festival", url);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(HTTP.CONTENT_TYPE, TYPE_CONTENT_JSON);
			HttpResponse httpResponse = null;

			try {
				httpResponse = httpClient.execute(httpPost);

				// check http protocol success
				int statusCode = httpResponse.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {

				}

				// check result is valid
				final String string = EntityUtils.toString(httpResponse
						.getEntity());
				JSONObject jsonObject = new JSONObject(string);
				lat = jsonObject.getString("lat");
				lng = jsonObject.getString("lng");

			} catch (Exception e) {
			} finally {
			}

			if (httpResponse == null) {

			}
			return null;

		}

	}

	private static class GetFestivalsTask extends AsyncTask<String, Void, Void> {
		public OnResponse res;
		final ArrayList<Festival> mFestivals = new ArrayList<Festival>();

		public GetFestivalsTask(OnResponse res) {
			this.res = res;
		}

		@Override
		protected void onPostExecute(Void result) {
			res.onResponse(mFestivals);
		}

		protected Void doInBackground(String... urls) {
			Log.e("Festival",urls[0]);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);

			// prepare HttpPost
			HttpPost httpPost = new HttpPost(urls[0]);
			httpPost.setHeader(HTTP.CONTENT_TYPE, TYPE_CONTENT_JSON);
			HttpResponse httpResponse = null;

			try {
				httpResponse = httpClient.execute(httpPost);

				// check http protocol success
				int statusCode = httpResponse.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {

				}

				// check result is valid
				final String string = EntityUtils.toString(httpResponse
						.getEntity());
				JSONArray jsonArray = new JSONArray(string);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					final String name = json.getString("name")
							.replaceAll("\n", "").replace("\n", "")
							.replace("\r", "").replaceAll("  ", " ");
					final String img = json.getString("img");
					final String date = json.getString("date");
					final String url = json.getString("url");
					final String place = json.getString("place");
					final String start_date = json.getString("start_date");
					final String end_date = json.getString("end_date");
					String content;
					try {
						content = json.getString("content");
					} catch (Exception e) {
						content = "";
					}
					mFestivals.add(new Festival(name, img, date, url, content,
							place, start_date, end_date));
				}

			} catch (Exception e) {
			} finally {
			}

			if (httpResponse == null) {

			}
			return null;

		}

	}

	@SuppressLint("NewApi")
	public static Bitmap apply(Context context, Bitmap sentBitmap, int radius) {
		if (VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

			final RenderScript rs = RenderScript.create(context);
			final Allocation input = Allocation.createFromBitmap(rs,
					sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			Allocation output = Allocation.createTyped(rs,
					input.getType());
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
					Element.U8_4(rs));
			script.setRadius(radius);
			script.setInput(input);
			script.forEach(output);
			output.copyTo(bitmap);
			output.destroy();
			output = null;
			sentBitmap.recycle();
			return bitmap;
		}

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java Author: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

	public static void openurl(Context context, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	public static void makeDialog(final Context context, Drawable bg,
			Bitmap bm, final Festival festival) {
		new GetLocationTask(festival)
				.execute(festival.place);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.activity_festival_info, null);
		layout.setBackgroundDrawable(bg);
		ImageView iv = (ImageView) layout.findViewById(R.id.iv_pic);
		ImageView ic = (ImageView) layout.findViewById(R.id.thumbnail);
		TextView name = (TextView) layout.findViewById(R.id.name);
		TextView appo = (TextView) layout.findViewById(R.id.appointment);
		TextView place = (TextView) layout.findViewById(R.id.tv_place);
		TextView content = (TextView) layout.findViewById(R.id.introduction);
		iv.setImageBitmap(bm);
		appo.setText(festival.date);
		ic.setImageBitmap(bm);
		name.setText(festival.name);
		place.setText(festival.place);
		appo.setFocusable(true);
		appo.setEllipsize(TruncateAt.MARQUEE);
		appo.setSelected(true);
		place.setFocusable(true);
		place.setEllipsize(TruncateAt.MARQUEE);
		place.setSelected(true);
		if (!festival.content.equals("null"))
			content.setText(festival.content);
		else
			layout.findViewById(R.id.ly_content).setVisibility(View.GONE);
		View gotoweb = layout.findViewById(R.id.gotoweb);
		View gotomap = layout.findViewById(R.id.gotomap);
		View calendar = layout.findViewById(R.id.calendar);
		gotoweb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Util.openurl(context, festival.url);

			}
		});

		calendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent nativeIntent = new Intent(Intent.ACTION_EDIT);
				nativeIntent.setType("vnd.android.cursor.item/event");

				nativeIntent.putExtra("beginTime",
						getDateInMs(festival.start_date));
				nativeIntent
						.putExtra("endTime", getDateInMs(festival.end_date));
				nativeIntent.putExtra("title", festival.name);

				context.startActivity(nativeIntent);

			}
		});
		final Dialog dialog = new Dialog(context, R.style.full_screen_dialog);
		gotomap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String lat = festival.lat;
				String lng = festival.lng;
				if (lat.trim().equals("null") || lng.trim().equals("null")) {
					Toast.makeText(context, "위치정보가 없습니다.", 0).show();
					return;
				}
				double latitude = Double.parseDouble(lat);
				double longitude = Double.parseDouble(lng);
				String label = "ABC Label";
				String uriBegin = "geo:" + latitude + "," + longitude;
				String query = festival.place;
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
				context.startActivity(intent);

			}
		});
		dialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
				LayoutParams.FLAG_FULLSCREEN);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setContentView(layout);
		dialog.show();

	}

	private static Long getDateInMs(String stringDateTime)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatter.parse(stringDateTime);
			long dateInLong = date.getTime();
			return dateInLong;
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;

	}
	
	private static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
        
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
 
        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
 
            bitmap.compress(CompressFormat.JPEG, 100, out);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
  }
}
