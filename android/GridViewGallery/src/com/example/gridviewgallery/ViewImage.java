package com.example.gridviewgallery;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewImage extends Activity {
	// constant value	
	private static final String ACTIVITY_TAG = "ViewImage";
	
	private TextView text;
	private ImageView imageview;
	private ProgressDialog pDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);

		Intent i = getIntent();
		String urlPath = i.getStringExtra("image");

		initView();

		new DisplayFileFromURL().execute(urlPath);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		text = (TextView) findViewById(R.id.fullImageName);
		imageview = (ImageView) findViewById(R.id.fullImage);
	}

	// Warning : HttpClient cannot process onCreate 
	class DisplayFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewImage.this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);// 取消進度條
			pDialog.setCancelable(true);// 開啟取消
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			Log.d(ACTIVITY_TAG, "start");					
			String imageUrl = args[0];
			String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.length());

			Log.d(ACTIVITY_TAG, "imageUrl : " + imageUrl);		
			Log.d(ACTIVITY_TAG, "imageName : " + imageName);
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(imageUrl);

			HttpResponse httpResponse;
			try {
				httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					BufferedHttpEntity b_entity = new BufferedHttpEntity(
							httpEntity);
					InputStream input = b_entity.getContent();

					Bitmap bitmap = BitmapFactory.decodeStream(input);
					
					text.setText(imageName);
					imageview.setImageBitmap(bitmap);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(ACTIVITY_TAG, "end");	
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if (result != null) {
				Toast.makeText(ViewImage.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

	}
	

}
