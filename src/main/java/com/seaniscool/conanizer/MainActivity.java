package com.seaniscool.conanizer;

import java.io.IOException;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;

/**
 * @author Sean Connolly
 */
public class MainActivity extends Activity implements
		TextureView.SurfaceTextureListener {

	private Camera camera;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextureView view = new TextureView(this);
		view.setSurfaceTextureListener(this);
		setContentView(view);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		camera = Camera.open();
		try {
			camera.setPreviewTexture(surface);
            camera.setDisplayOrientation(90);
			camera.startPreview();
		} catch (IOException ioe) {
			// Something bad happened
			ioe.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// Ignored, Camera does all the work for us
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		camera.stopPreview();
		camera.release();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// do nothing
	}

}
