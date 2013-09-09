package com.seaniscool.conanizer;

import android.content.Context;
import android.graphics.*;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.util.Log;
import android.view.View;

/**
 * @author Sean Connolly
 */
public class HairView extends View implements Camera.PreviewCallback {

	private static final String TAG = HairView.class.getSimpleName();
	private static final int NUM_FACES = 3;
	private final Paint paint;
	private final Face[] faces;

	HairView(Context context) {
		super(context);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		faces = new Face[NUM_FACES];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		PointF midpoint = new PointF();
		for (FaceDetector.Face face : faces) {
			if (face != null) {
				face.getMidPoint(midpoint);
				float width = face.eyesDistance();
				float halfWidth = width / 2;
				float startX = midpoint.x - halfWidth;
				float endX = midpoint.x + halfWidth;
				float startY = midpoint.y;
				float endY = midpoint.y;
				canvas.drawLine(startX, startY, endX, endY, paint);
			}
		}
		super.onDraw(canvas);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPreviewFrame(byte[] bytes, Camera camera) {
		Log.e(TAG, "Previewing: " + bytes.length + "b");
		Camera.Size size = camera.getParameters().getPreviewSize();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
				options);
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Log.e(TAG, "bitmap: " + bitmap);
		Log.e(TAG, "matrix: " + matrix);
		Log.e(TAG, "size: " + size);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, size.width, size.height,
				matrix, true);
		FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(),
				bitmap.getHeight(), 1);
		int foundFaces = faceDetector.findFaces(bitmap, faces);
		if (foundFaces > 0) {
			Log.i(TAG, "Found a face!");
		} else {
			Log.i(TAG, "No face found!");
		}
	}

	private void detectFaces(Bitmap image) {
		FaceDetector arrayFaces = new FaceDetector(image.getWidth(),
				image.getHeight(), NUM_FACES);
		int facesFound = arrayFaces.findFaces(image, faces);
		Log.e(TAG, "Found faces: " + facesFound);
		invalidate();
	}

}
