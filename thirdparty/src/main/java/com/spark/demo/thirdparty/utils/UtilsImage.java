package com.spark.demo.thirdparty.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.opengl.GLException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Wangxiaxin on 2015/8/17.
 * <p>
 * 图片工具类
 */
public class UtilsImage extends AbsUtil {

    private static final int QUALITY_STEP = 10;

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 自动处理旋转
     *
     * @param currentDegree 当前的旋转角度
     * @param bitmap        当前的图片
     */
    public static Bitmap autoRotate(int currentDegree, Bitmap bitmap) {

        if (currentDegree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(currentDegree);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
            if (bitmap2 != null) {
                bitmap.recycle();
                return bitmap2;
            } else {
                return bitmap;
            }
        } else {
            return bitmap;
        }
    }

    /**
     * 将bitmap按照最大的file size保存到指定文件
     *
     * @param picFilePath 待保存的文件路径
     * @param bitmap      图片
     * @param maxSize     限制保存后的最大大小 单位B
     */
    public static void saveBitmapToFileWithCompress(String picFilePath, Bitmap bitmap,
                                                    final int maxSize) {
        File photoFile = new File(picFilePath); // 在指定路径下创建文件
        FileOutputStream fileOutputStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int scale = 100;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (bitmap != null) {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, scale, baos)) {
                    int baosSize = baos.toByteArray().length;
                    while (baosSize > maxSize && scale > 0) {
                        baos.reset();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, scale, baos);
                        baosSize = baos.toByteArray().length;
                        scale -= QUALITY_STEP;
                    }
                    // 缩放后的数据写入到文件中
                    baos.writeTo(fileOutputStream);
                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 创建GLSurface显示内容的截图
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param gl
     * @return
     */
    public static Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                    intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }
}
