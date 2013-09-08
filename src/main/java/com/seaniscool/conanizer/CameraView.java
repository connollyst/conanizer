package com.seaniscool.conanizer;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Sean Connolly
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder mHolder;
	Camera mCamera;

	CameraView(Context context) {
		super(context);
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
//            mCamera.startFaceDetection();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		mCamera.stopPreview();
		mCamera = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
//		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setPreviewSize(w, h);
//		mCamera.setParameters(parameters);
//		mCamera.startPreview();
	}

}
