package com.example.gridviewgallery;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
	// constant value
	private static final String ACTIVITY_TAG = "Gallery";
	private static final String LIST_URL = "http://192.168.56.1/download/list.php";
	
	// JSON element ids from repsonse of php script:	
	//private static final String JSON_TAG_COUNT = "count";
	private static final String JSON_TAG_FILENAME = "filename";
	
	private GridView gridView = null;
	private GridViewAdapter gridAdapter;
	private Context mContext;
	private ProgressDialog pDialog = null;
	private JSONParser jsonParser;

	// Data
	ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
	ArrayList<String> images = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		jsonParser = new JSONParser();

		initGridView();
		initGridViewEvent();
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
		case R.id.quit:
			finish();
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
		inflater.inflate(R.menu.options_menu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * initialize : GridView 的初始化
	 * 
	 */
	private void initGridView() {
		// TODO Auto-generated method stub
		gridView = (GridView) findViewById(R.id.gridView);

		new DownloadFileFromURL().execute(LIST_URL);

	}

	/**
	 * GridView : GridView 的事件處理,當點選時跳至 ViewImage
	 * 
	 */
	private void initGridViewEvent() {
		// TODO Auto-generated method stub
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ViewImage.class);

				i.putExtra("image", images.get(position));

				startActivity(i);
			}
		});
	}

	// Warning : HttpClient cannot process onCreate 
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);// 取消進度條
			pDialog.setCancelable(true);// 開啟取消
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			Log.d(ACTIVITY_TAG, "Loading starting");
			String urlPath = args[0];
			JSONObject json = jsonParser.makeHttpRequest(urlPath, "POST", null);
			if (json == null) {
				return "connection error";
			}
			Log.d(ACTIVITY_TAG, "Loading attempt : " + json.toString());

			try {

				JSONArray numberList = json.getJSONArray("images");

				String imageUrl = urlPath.substring(0,
						urlPath.lastIndexOf('/') + 1);

				for (int i = 0; i < numberList.length(); i++) {
					String imageName = numberList.getJSONObject(i).getString(
							JSON_TAG_FILENAME);
					images.add(imageUrl + imageName);
					Log.d(ACTIVITY_TAG, "Image : " + images.get(i));

					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(images.get(i));

					HttpResponse httpResponse;
					try {
						httpResponse = httpClient.execute(httpGet);
						if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							HttpEntity httpEntity = httpResponse.getEntity();
							BufferedHttpEntity b_entity = new BufferedHttpEntity(
									httpEntity);
							InputStream input = b_entity.getContent();

							Bitmap bitmap = BitmapFactory.decodeStream(input);
							imageItems.add(new ImageItem(bitmap, imageName));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			gridAdapter = new GridViewAdapter(mContext, imageItems);

			return "Done";
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			// setAdapter must be set on onPostExecute
			// http://goo.gl/RsEbmB
			gridView.setAdapter(gridAdapter);

			if (result != null) {
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

}
