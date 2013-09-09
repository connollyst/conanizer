package com.seaniscool.conanizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Sean Connolly
 */
public class MainActivity extends Activity {

	private HairView hairView;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hairView = new HairView(this);
		CameraView view = new CameraView(this);
		setContentView(view);
		addContentView(hairView);
	}

	public void addContentView(View view) {
		addContentView(view, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public HairView getHairView() {
		return hairView;
	}

}
