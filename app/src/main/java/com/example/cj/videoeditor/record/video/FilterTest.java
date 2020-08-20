package com.example.cj.videoeditor.record.video;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FilterTest {

  private int mWidth = 720;
  private int mHeight = 1080;

  // RGB color values for generated frames
  private static final int TEST_R0 = 0;
  private static final int TEST_G0 = 136;
  private static final int TEST_B0 = 0;
  private static final int TEST_R1 = 236;
  private static final int TEST_G1 = 50;
  private static final int TEST_B1 = 186;

  private int frameIndex = 0;

  public void generateSurfaceFrame() {
    generateSurfaceFrame(frameIndex);
    frameIndex++;
  }

  public FilterTest() {
    mPixelBuf = ByteBuffer.allocateDirect(mWidth * mHeight * 4);
    mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
  }


  private void generateSurfaceFrame(int frameIndex) {
    frameIndex %= 8;

    int startX, startY;
    if (frameIndex < 4) {
      // (0,0) is bottom-left in GL
      startX = frameIndex * (mWidth / 4);
      startY = mHeight / 2;
    } else {
      startX = (7 - frameIndex) * (mWidth / 4);
      startY = 0;
    }

    GLES20.glClearColor(TEST_R0 / 255.0f, TEST_G0 / 255.0f, TEST_B0 / 255.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
    GLES20.glScissor(startX, startY, mWidth / 4, mHeight / 2);
    GLES20.glClearColor(TEST_R1 / 255.0f, TEST_G1 / 255.0f, TEST_B1 / 255.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
  }

  private ByteBuffer mPixelBuf;                       // used by saveFrame()

  /**
   * Saves the current frame to disk as a PNG image.
   * <p>
   * 将SurfaceTexture的数据保存到内存卡中
   */
  public void saveFrame(String filename) throws IOException {

    mPixelBuf.rewind();
    GLES20.glReadPixels(0, 0, mWidth, mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
            mPixelBuf);

    BufferedOutputStream bos = null;
    try {
      bos = new BufferedOutputStream(new FileOutputStream(filename));
      Bitmap bmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
      mPixelBuf.rewind();
      bmp.copyPixelsFromBuffer(mPixelBuf);
      bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
      bmp.recycle();
    } finally {
      if (bos != null) bos.close();
    }
    Log.d("debug_log", "Saved " + mWidth + "x" + mHeight + " frame as '" + filename + "'");
  }
}
