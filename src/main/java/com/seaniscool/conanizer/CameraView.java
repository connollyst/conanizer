package com.seaniscool.conanizer;

import java.io.IOException;

import android.graphics.Matrix;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Sean Connolly
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	private int rotation = 90;

	private Camera camera;
	private HairView hairView;

	CameraView(MainActivity context) {
		super(context);
		this.hairView = context.getHairView();
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
			camera.setPreviewDisplay(holder);
			camera.setDisplayOrientation(rotation);
			camera.startPreview();
			camera.startFaceDetection();
			camera.setFaceDetectionListener(hairView);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		hairView.setMatrix(createTransformationMatrix());
	}

	/**
	 * Camera driver coordinates range from (-1000, -1000) to (1000, 1000).<br/>
	 * UI coordinates range from (0, 0) to (width, height).
	 */
	private Matrix createTransformationMatrix() {
		Matrix matrix = new Matrix();
		// Need mirror for front camera.
		boolean mirror = true;
		// (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
		matrix.setScale(mirror ? -1 : 1, 1);
		matrix.postRotate(rotation);
		matrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
		matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
		return matrix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopFaceDetection();
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
