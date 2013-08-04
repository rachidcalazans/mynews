package br.com.maxwellfn.mynews;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;

	public DownloadImageTask(ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {

//			if (urldisplay != null && urldisplay.contains("http://")) {

				// InputStream in = new java.net.URL(urldisplay.replaceAll(" ",
				// "%20")).openStream();
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
//			} else {
			// Log.d("MYNEWS", "urldisplay=" + urldisplay);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		bmImage.setImageBitmap(result);
	}
}
