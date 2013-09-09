package com.seaniscool.conanizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;

/**
 * @author Sean Connolly
 */
public class HairView extends View implements Camera.FaceDetectionListener {

	private Camera.Face[] faces;
	private final Paint paint;
	private Matrix matrix;

	HairView(Context context) {
		super(context);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Log.e(getClass().getSimpleName(), "Drawing");
		if (faces != null) {
			Log.e(getClass().getSimpleName(), "Drawing faces");
			for (Camera.Face face : faces) {
				if (face.rect != null) {
					int left = face.rect.left;
					int right = face.rect.right;
					int top = face.rect.top;
					int bottom = face.rect.bottom;
					float[] coord = new float[] { left, right, top, bottom };
					matrix.mapPoints(coord);
					left = (int) coord[0];
					right = (int) coord[1];
					top = (int) coord[2];
					bottom = (int) coord[3];
					canvas.drawRect(left, top, right, bottom, paint);
				}
			}
		}
		super.onDraw(canvas);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFaceDetection(Camera.Face[] faces, Camera camera) {
		this.faces = faces;
		for (Camera.Face face : faces) {
			if (face.leftEye != null && face.rightEye != null
					&& face.mouth != null) {
				Log.e(getClass().getSimpleName(), "");
			}
		}
		invalidate();
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

}
