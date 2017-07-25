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
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.io.IOException;


public class CircleImage extends ImageView {

      private int borderWidth;
      private  Bitmap image;
      private  Paint shadowPaint = new Paint();

      private Paint paint = new Paint(), paintBorder = new Paint();
      private static final ScaleType SCALE_TYPE = ScaleType.FIT_XY;
      private int shadowColor = Color.BLACK;
      private float shadowRadius;

      private  BitmapShader bitmapShader;
      private int borderRadius;
      private Matrix matrix  = new Matrix();
      private final float DEFAULT_SHADOW_RADIUS = 10;

      private int drawRadius;
      private int bitmapWidth, bitmapHeight;
      private int color;
      private boolean addShadow;
      private Context context;


    public CircleImage(Context context) {
        super(context);
       // setScaleType(ScaleType.MATRIX);
        this.context = context;
    }

    public CircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
       // setScaleType(ScaleType.MATRIX);
        this.context = context;
    }

    public CircleImage(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImage, i, 0);
        borderWidth = a.getDimensionPixelSize(R.styleable.CircleImage_border_width, 0);
        color = a.getColor(R.styleable.CircleImage_border_color, Color.WHITE);
        addShadow = a.getBoolean(R.styleable.CircleImage_add_shadow, false);
        shadowColor = a.getColor(R.styleable.CircleImage_shadow_color, Color.BLACK);
        shadowRadius = a.getFloat(R.styleable.CircleImage_shadow_radius, DEFAULT_SHADOW_RADIUS);
        a.recycle();
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

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        image = getImageFromUri(uri);
        setup();
    }

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
            paintBorder.setShadowLayer(shadowRadius, 0, 3, shadowColor);
        }

        scaleImage();
        invalidate();
    }


    private Bitmap getImageFromUri(Uri uri) {
        Bitmap temp = null;
        try {
            temp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException exception){
            exception.printStackTrace();
        }

        return temp;

    }


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

    private void drawShadow() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        }

        paintBorder.setShadowLayer(shadowRadius, 0.0f, shadowRadius/2, shadowColor);
    }

    public void loadHighResolutionImage(String filePath) {

        ImageCompress imageCompress = new ImageCompress(context);
        imageCompress.getLowBitmap(filePath);
        imageCompress.setOnInformImage(new ImageCompress.InformImage() {
            @Override
            public void getLowLevelBitmap(Bitmap lowBitmmap) {
                image = lowBitmmap;
                setup();
            }
        });

    }


}
