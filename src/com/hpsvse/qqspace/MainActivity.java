package com.hpsvse.qqspace;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.app.Activity;

public class MainActivity extends Activity {

	private ScrollListView mScrollListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mScrollListView = (ScrollListView) findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] {
						"1111111111111111111111111",
						"2222222222222222222222222",
						"3333333333333333333333333",
						"444444444444444444444444",
						"5555555555555555555555555",
						"66666666666666666666666",});
		
		
		View head = View.inflate(this, R.layout.topimg_main, null);
		ImageView iv = (ImageView) head.findViewById(R.id.layout_header_image);
		mScrollListView.setZoomImageView(iv);
		mScrollListView.addHeaderView(head);
		mScrollListView.setAdapter(adapter);
		
	}

}
