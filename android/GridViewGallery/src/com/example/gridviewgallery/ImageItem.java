package com.example.gridviewgallery;

import android.graphics.Bitmap;

public class ImageItem {
	private String title;
	private Bitmap image;

	public ImageItem(Bitmap image, String title) {
		this.title = title;
		this.image = image;
	}

	public Bitmap getImage() {
		return this.image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
