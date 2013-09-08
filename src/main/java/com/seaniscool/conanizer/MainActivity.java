package com.seaniscool.conanizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * @author Sean Connolly
 */
public class MainActivity extends Activity {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CameraView view = new CameraView(this);
		setContentView(view);
		addContentView(new HairView(this), new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
	}

}
