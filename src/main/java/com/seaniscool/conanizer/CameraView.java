package com.seaniscool.conanizer;

import java.io.IOException;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Sean Connolly
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private final HairView hairView;
	private Camera camera;

	CameraView(MainActivity context) {
		super(context);
		hairView = context.getHairView();
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
		try {
			camera.setPreviewCallback(hairView);
			camera.setPreviewDisplay(holder);
			camera.setDisplayOrientation(90);
			camera.startPreview();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		// Camera.Parameters parameters = camera.getParameters();
		// parameters.setPreviewSize(w, h);
		// camera.setParameters(parameters);
		// camera.startPreview();
	}

}
