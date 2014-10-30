package com.example.gridviewgallery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewImage extends Activity {
	// constant value	
	private static final String ACTIVITY_TAG = "ViewImage";
	
	private TextView text;
	private ImageView imageview;
	private ProgressDialog pDialog = null;
	private String urlPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);

		Intent i = getIntent();
		urlPath = i.getStringExtra("image");

		initView();

		new DisplayFileFromURL().execute(urlPath);
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		text = (TextView) findViewById(R.id.fullImageName);
		imageview = (ImageView) findViewById(R.id.fullImage);
	}

	/**
	 * Menu : 第一次建立 menu的時候呼叫的
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Menu : 點選 menu item 的處理
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.view_menu_download:
			String filePathName  = Environment.getExternalStorageDirectory().getAbsolutePath();
			String imageName = urlPath.substring(urlPath.lastIndexOf('/') + 1, urlPath.length());
			new DownloadFileFromURL().execute(urlPath, filePathName + "/" + imageName);
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Menu : 每次點擊 menu的時候匯入 option_menu.xml的資料，注意要先清除先前的資料 不然 menu item 會累加上去
	 * 
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_menu, menu);
		return super.onPrepareOptionsMenu(menu);
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
			pDialog.setMessage(getResources().getString(R.string.image_view_download_progress_dialog_context));
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
	

	public void Notification() {
		// TODO Auto-generated method stub
		String strtitle = getResources().getString(R.string.image_view_notification_title);
		String strtext = getResources().getString(R.string.image_view_notification_text);
		//Intent i = new Intent(this, DetailActivity.class);
		
		//PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setTicker(strtext)
				.setContentTitle(strtitle)
				.setContentText(strtext)
				//.addAction(R.drawable.ic_stat_notification,"Action Button", pIntent)
				//.setContentIntent(pIntent)
				.setAutoCancel(true);
		
		NotificationManager notificationmanager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify(0, builder.build());
		
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewImage.this);
			pDialog.setMessage(getResources().getString(R.string.image_view_download_progress_dialog_context));
			pDialog.setIndeterminate(false);// 取消進度條
			pDialog.setCancelable(true);// 開啟取消
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			pDialog.setProgress(Integer.parseInt(values[0]));
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			OutputStream outputStream = null;
			long lengthOfFile;
			Log.d(ACTIVITY_TAG, "Download source : " + args[0]);
			Log.d(ACTIVITY_TAG, "Download target : " + args[1]);
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(args[0]);

				// Http connect success.
				HttpResponse httpResponse;
				httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
					lengthOfFile = httpEntity.getContentLength();
				} else {
					return null;
				}

				outputStream = new FileOutputStream(new File(args[1]));

				int read = 0;
				byte[] bytes = new byte[1024];
				long total = 0;

				while ((read = inputStream.read(bytes)) != -1) {
					total += read;
					publishProgress("" + (int) ((total * 100) / lengthOfFile));
					outputStream.write(bytes, 0, read);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(ACTIVITY_TAG, e.getMessage());
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(ACTIVITY_TAG, e.getMessage());
					}
				}

				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(ACTIVITY_TAG, e.getMessage());
					}
				}
			}


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
			Notification();
			if (result != null) {
				Toast.makeText(ViewImage.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}
