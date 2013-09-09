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
		// if (bitmap != null) {
		// Log.e(TAG,
		// "Drawing bitmap @ " + bitmap.getWidth() + "x"
		// + bitmap.getHeight());
		// canvas.drawBitmap(bitmap,
		// new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
		// new Rect(0, 0, getWidth(), getHeight()), paint);
		// }
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
//		Log.e(TAG, "Previewing: " + bytes.length + "b");
		Camera.Size size = camera.getParameters().getPreviewSize();
		int[] argb8888 = new int[bytes.length];
		decodeYUV(argb8888, bytes, size.width, size.height);
        Bitmap bitmap = Bitmap.createBitmap(argb8888, size.width, size.height,
				Bitmap.Config.ARGB_8888);
		Matrix matrix = new Matrix();
		matrix.preRotate(-90);
//		Log.e(TAG, "bitmap: " + bitmap);
//		Log.e(TAG, "matrix: " + matrix);
//		Log.e(TAG, "size: " + size);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, size.width, size.height,
				matrix, true);
		FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(),
				bitmap.getHeight(), 1);
		int foundFaces = faceDetector.findFaces(bitmap, faces);
		if (foundFaces > 0) {
			Log.e(TAG, "Found a face!");
		} else {
			Log.e(TAG, "No face found!");
		}
		invalidate();
	}

	/**
	 * Decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP
	 * by David Manpearl 081201
	 * https://groups.google.com/forum/#!topic/android-developers/yF6CmrIJzuo
	 *
	 * @param out
	 * @param fg
	 * @param width
	 * @param height
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public void decodeYUV(int[] out, byte[] fg, int width, int height)
			throws NullPointerException, IllegalArgumentException {
		int sz = width * height;
		if (out == null)
			throw new NullPointerException("buffer out is null");
		if (out.length < sz)
			throw new IllegalArgumentException("buffer out size " + out.length
					+ " < minimum " + sz);
		if (fg == null)
			throw new NullPointerException("buffer 'fg' is null");
		if (fg.length < sz)
			throw new IllegalArgumentException("buffer fg size " + fg.length
					+ " < minimum " + sz * 3 / 2);
		int i, j;
		int Y, Cr = 0, Cb = 0;
		for (j = 0; j < height; j++) {
			int pixPtr = j * width;
			final int jDiv2 = j >> 1;
			for (i = 0; i < width; i++) {
				Y = fg[pixPtr];
				if (Y < 0)
					Y += 255;
				if ((i & 0x1) != 1) {
					final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
					Cb = fg[cOff];
					if (Cb < 0)
						Cb += 127;
					else
						Cb -= 128;
					Cr = fg[cOff + 1];
					if (Cr < 0)
						Cr += 127;
					else
						Cr -= 128;
				}
				int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
				if (R < 0)
					R = 0;
				else if (R > 255)
					R = 255;
				int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
						+ (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
				if (G < 0)
					G = 0;
				else if (G > 255)
					G = 255;
				int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
				if (B < 0)
					B = 0;
				else if (B > 255)
					B = 255;
				out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
			}
		}

	}

}
