package br.com.rotteny.yohrebanho.handlers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.Display;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by ENCBACK on 11/07/2017.
 */

public class BitmapHandler {

    private static int maxSize = 512;

    public static Bitmap bitmapResize(Bitmap bitmap) {

        int newWidth    = (int) bitmap.getWidth();
        int newHeight   = (int) bitmap.getHeight();

        if(newHeight > newWidth) {
            if(newHeight > maxSize) {
                newWidth    = (int) (( newWidth * maxSize) / newHeight );
                newHeight   = maxSize;
            }
        }
        else {
            if(newWidth > maxSize) {
                newHeight   = (int) (( newHeight * maxSize) / newWidth );
                newWidth    = maxSize;
            }
        }

        return Bitmap.createScaledBitmap(
                bitmap,
                newWidth,
                newHeight,
                true);
    }

    public static Bitmap decodeSampledBitmap(String pathName,Display display) {
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return decodeSampledBitmap(pathName, width, height);
    }

    private static Bitmap decodeSampledBitmap(String pathName, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap  = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }

    public static String encodeImageViewTobase64(ImageView image) {
        Bitmap bitmap  = ((BitmapDrawable) image.getDrawable()).getBitmap();
        return encodeTobase64(bitmap);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
