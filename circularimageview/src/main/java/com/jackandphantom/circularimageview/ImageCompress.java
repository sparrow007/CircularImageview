package com.jackandphantom.circularimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;

class ImageCompress {

    interface InformImage {
        void getLowLevelBitmap(Bitmap lowBitmmap);
    }

    private InformImage informImage;
    Context context;
    private Bitmap newBitmap;

    public ImageCompress(Context context) {
        this.context = context;
    }

    protected Bitmap getLowBitmap(String uri) {

        new ImgCompression(uri).execute();
        return newBitmap;
    }

    protected void setOnInformImage(InformImage informImage) {
        this.informImage = informImage;
    }

    private class ImgCompression extends AsyncTask<Void , Void, Void> {
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;
        private String string;

        public ImgCompression(String uri) {
            string = uri;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (compressImage() != null) {
                newBitmap = compressImage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (informImage != null)
                informImage.getLowLevelBitmap(newBitmap);


        }

        private Bitmap compressImage() {

            Bitmap scaledBitmap = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            Bitmap  bm  =  BitmapFactory.decodeFile(this.string, options);


            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / actualHeight;
            float maxRatio  = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {

                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                }
                else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                }
                else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }

            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inTempStorage = new byte[16 * 1024];
            try {
                bm = BitmapFactory.decodeFile(string, options);
            } catch (OutOfMemoryError exc) {
                exc.printStackTrace();
            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            }catch (OutOfMemoryError exp) {
                exp.printStackTrace();
            }
            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bm, middleX - bm.getWidth()/2, middleY - bm.getHeight()/2, new Paint(Paint.FILTER_BITMAP_FLAG));
            bm.recycle();

            return scaledBitmap;
        }

        private int calculateInSampleSize(BitmapFactory.Options options, int actualWidth, int actualHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > actualHeight || width > actualWidth) {
                final int widthRatio = Math.round((float) width / (float) actualWidth);
                final int heightRatio = Math.round((float) height / (float) actualHeight);
                inSampleSize = (heightRatio < widthRatio)? heightRatio : widthRatio;
            }
            final  float totalPixels = width * height;
            final float totalReqPixels = actualHeight * actualWidth * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixels) {
                inSampleSize++;
            }
            return inSampleSize;
        }


    }


}
