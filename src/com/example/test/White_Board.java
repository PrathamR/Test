package com.example.test;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Menu;

public class White_Board extends Activity {
	DrawView drawView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_white__board);
		 drawView = new DrawView(this);
	        drawView.setBackgroundColor(Color.WHITE);
	        setContentView(drawView);
	        drawView.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.white__board, menu);
		return true;
	}

}
