package com.example.gridviewgallery;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ImageItem> data;

	public GridViewAdapter(Context context, ArrayList<ImageItem> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void erase() {
		// TODO Auto-generated method stub
        Iterator<ImageItem> it = data.iterator();
        while (it.hasNext()) {
        	it.remove();
        }
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.view_image_grid, parent, false);

			// save components to ViewHolder
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.gridText);
			holder.image = (ImageView) row.findViewById(R.id.gridImage);
			row.setTag(holder);
		} else {
			// get components from ViewHolder
			holder = (ViewHolder) row.getTag();
		}

		ImageItem item = data.get(position);
		holder.imageTitle.setText(item.getTitle());
		holder.image.setImageBitmap(item.getImage());		
		return row;
	}

	static class ViewHolder {
		ImageView image;
		TextView imageTitle;
	}

}
