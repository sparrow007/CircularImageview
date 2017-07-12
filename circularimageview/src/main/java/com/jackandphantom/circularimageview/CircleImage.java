package com.jackandphantom.circularimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class CircleImage extends ImageView {

      private int borderWidth;
      private  Bitmap image;
      private  Paint shadowPaint = new Paint();
      private final int DEFAULT_SHADOW_DX = 0;
      private final int DEFAULT_SHADOW_DY = 3;
      private final float DEFAULT_SHADOW_RADIUS = 10;

      private Paint paint = new Paint(), paintBorder = new Paint();
      private static final ScaleType SCALE_TYPE = ScaleType.FIT_XY;
      private int shadowColor = Color.BLACK;


      private  BitmapShader bitmapShader;
      private int borderRadius;
      private Matrix matrix  = new Matrix();
      private float shadowRadius = DEFAULT_SHADOW_RADIUS;

      private int drawRadius;
      private int bitmapWidth, bitmapHeight;
      private int color = Color.WHITE;
      private boolean addShadow;


    public CircleImage(Context context) {
        super(context);
       // setScaleType(ScaleType.MATRIX);
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
       // setScaleType(ScaleType.MATRIX);

    }
    /* Getting the value from xml  */
    public CircleImage(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImage, i, 0);
        try {
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImage_border_width, 0);
            color = a.getColor(R.styleable.CircleImage_border_color, Color.WHITE);
            addShadow = a.getBoolean(R.styleable.CircleImage_add_shadow, false);
            shadowColor = a.getColor(R.styleable.CircleImage_shadow_color, Color.BLACK);
            shadowRadius = a.getFloat(R.styleable.CircleImage_shadow_radius, DEFAULT_SHADOW_RADIUS);
        }
       finally {
            a.recycle();
        }

        setup();
    }



    /*this will draw one circle for border and another for image*/
    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

            canvas.drawCircle(getWidth()/2, getHeight()/2, borderRadius , paintBorder );
            canvas.drawCircle(getWidth()/2, getHeight()/2, drawRadius , paint );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        getDrawableToBitmap(getDrawable());
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getDrawableToBitmap(drawable);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        image = bm;
        setup();
    }

    /* this method used for initialize of both the paint and paint border which is used to draw circle
     * this method also used for adding shadow effect
     */
    private void setup() {
        if (image == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }

        paint.setAntiAlias(true);
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(color);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderWidth);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setStrokeWidth(borderWidth);
        shadowPaint.setColor(Color.LTGRAY);
        bitmapWidth = image.getWidth();
        bitmapHeight = image.getHeight();
        bitmapShader = new BitmapShader(image,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        borderRadius = Math.min((getWidth()-borderWidth)/2, (getHeight()- borderWidth)/2);
        drawRadius = Math.min((getWidth() -( 2 * borderWidth)) / 2, (getHeight() -(borderWidth*2)) / 2);

        if (addShadow) {
            borderRadius -= shadowRadius;
            drawRadius -= shadowRadius;
            paintBorder.setShadowLayer(shadowRadius, DEFAULT_SHADOW_DX, DEFAULT_SHADOW_DY, shadowColor);
        }

        scaleImage();
        invalidate();
    }

    /* this method is used from getting the bitmap from drawable */
    private void getDrawableToBitmap(Drawable drawable) {

        //return if drawable is null that means it doen't have a bitmap
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);
        image = bitmap;
    }

    /* getter and setter region */
    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        setup();
    }

    public int getBorderColor() {
        return this.color;
    }

    public void setBorderColor(int color) {
        this.color = color;
        setup();
    }
    public void setAddShadow(boolean shadow) {
        this.addShadow = shadow;
    }

    public boolean getAddShadow() {
        return addShadow;
    }

    public void setShadowRadius(float shadowradius) {
        this.shadowRadius = shadowradius;
    }

    public float getShadowRadius() {
       return this.shadowRadius;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    /* this mehtod used for scaling and translating the image in circle */
    private void scaleImage() {

        float dx = 0;
        float dy = 0;
        float scale;

        matrix.set(null);

       /* You can also get view width  by substracting both sides padding

       int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getHeight() - getPaddingLeft() - getPaddingRight();
        */

        if (bitmapWidth * getHeight() >  bitmapHeight * getWidth()) {
            scale = (float) getHeight() / (float) bitmapHeight;
            dx =  ((getWidth() - bitmapWidth * scale) * 0.5f);

        }
        else {
            scale = (float) getWidth() / (float) bitmapWidth;

            dy =  (getHeight()- bitmapHeight * scale) * 0.5f;

        }
        matrix.setScale(scale, scale);

        matrix.postTranslate((int) (dx + 0.5f) + borderWidth, (int) (dy + 0.5f) + borderWidth);

        bitmapShader.setLocalMatrix(matrix);
    }


}
