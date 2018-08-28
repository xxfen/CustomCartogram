package xxf.com.customcartogram.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xxf.com.customcartogram.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class AnnularView extends View {
    private float sum;//总数
    private Context context;
    //屏幕宽高
    //  private float width;
    //  private float height;
    //画笔
    private Paint mPaint;
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint textPaint;
    //控件宽高
    private int mWith;
    private int mHeight;

    //圆直径
    private float diameter;

    private Rect oRect;
    private Paint tPaint;
    private int multiple = 1;
    private Float[] datas = new Float[]{20f, 50f, 70f, 100f, 10f, 30f};
    private Float[] angle;
    // private Float[] names = new Float[]{20f, 50f, 70f, 100f, 10f, 30f};
    private String[] colorstr = new String[]{
            "#99ff66", "#ffff66", "#ff9966",
            "#ff3366", "#ccff99", "#cc6699",
            "#999999", "#33cc99", "#003399",
            "#ff33cc", "#cccccc", "#99cccc"};
    private Float startAngle = 0f, sweepAngle = 0f;
    private int angleColor;
    private int overallMultiple = 150;
    private List<Region> regionList;
    private int index = -1;

    public AnnularView(Context context) {
        super(context);
        this.context = context;
    }


    public AnnularView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        initdata();
    }

    private void initdata() {
        //    angle = new Float[datas.length];
        //    for (int i = 0; i < datas.length; i++) {
        //        sum = sum + datas[i];
        //    }
        //    for (int i = 0; i < datas.length; i++) {
        //        angle[i] = 360 * (datas[i] / sum);
        //    }
    }

    private void init() {
        regionList = new ArrayList<>();

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);       //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(1f);//设置画笔宽度为10px
        //mPaint.setAlpha(250);
        mPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(5f);

        mPaint1 = new Paint();
        mPaint1.setColor(Color.WHITE);       //设置画笔颜色
        mPaint1.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint1.setStrokeWidth(5f);         //设置画笔宽度为10px
        // mPaint1.setAlpha(115);
        mPaint1.setAntiAlias(true);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);       //设置画笔颜色
        mPaint2.setStyle(Paint.Style.STROKE);  //设置画笔模式为填充
        mPaint2.setStrokeWidth(5f);         //设置画笔宽度为10px
        // mPaint1.setAlpha(115);
        mPaint2.setAntiAlias(true);

        getScreenSize();


        //-------------
        oRect = new Rect();
        tPaint = new Paint();
        tPaint.setAntiAlias(true);
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setTextSize(30);
        tPaint.setColor(Color.BLACK);
        tPaint.setAntiAlias(true);
        tPaint.setStrokeWidth(4);
    }

    /**
     * 获取屏幕宽高
     */
    private void getScreenSize() {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        // width = dm.widthPixels;
        // height = dm.heightPixels;
        // diameter = width * 9f / 10f;//

        float screenWidth = dm.widthPixels * density;
        float screenHeight = dm.heightPixels * density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWith = MeasureSpec.getSize(widthMeasureSpec);    //取出宽度的确切数值
        mHeight = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        diameter = mWith > mHeight ? mHeight * 9 / 10 : mWith * 9 / 10;
    }

    private void drawArc(Canvas canvas, RectF rectF, float startAngle, float sweepAngle, int color) {

        Path rectFPath = new Path();
        rectFPath.moveTo(0, 0);
        // rectFPath.moveTo(width / 2, width / 2 + height / 20);
        rectFPath.lineTo(((float) Math.cos(Math.toRadians(startAngle))) * diameter / 2,
                ((float) Math.sin(Math.toRadians(startAngle))) * diameter / 2);
        RectF rect = new RectF(-diameter / 2, -diameter / 2, diameter / 2, diameter / 2);
        rectFPath.addArc(rect, startAngle,
                sweepAngle);
        rectFPath.lineTo(0, 0);
        rectFPath.close();
        rectFPath.computeBounds(rect, true);
        // rectFPath.moveTo(width / 2, width / 2 + height / 20);
        Region mRegion = new Region();
        mRegion.setPath(rectFPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        if (overallMultiple == multiple) {
            regionList.add(mRegion);
        }

        mPaint.setColor(color);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint2);

        drawArcText(canvas, ((int) (sweepAngle / 360 * 10000)) / 100f + "%", ((float) Math.cos(Math.toRadians(startAngle + sweepAngle / 2))) * diameter * 4 / 10,
                ((float) Math.sin(Math.toRadians(startAngle + sweepAngle / 2))) * diameter * 4 / 10);

    }

    private void drawCentreText(Canvas canvas, String text) {
        tPaint.getTextBounds(text, 0, text.length(), oRect);
        /*
         * 控件宽度/2 - 文字宽度/2
         */
        float v = tPaint.measureText(text);//文字宽度
        float startX = -v / 2;

        /*
         * 控件高度/2 + 文字高度/2,绘制文字从文字左下角开始,因此"+"
         */
        // float startY = getHeight() / 2 + oRect.height() / 2;
        Paint.FontMetricsInt fm = tPaint.getFontMetricsInt();
        //fm.bottom - fm.top//文字高度
        int startY = -fm.descent + (fm.bottom - fm.top) / 2;
        // 绘制文字
        canvas.drawText(text, startX, startY, tPaint);
    }

    private void drawArcText(Canvas canvas, String text, Float CentreX, Float CentreY) {
        tPaint.getTextBounds(text, 0, text.length(), oRect);
        /*
         * 控件宽度/2 - 文字宽度/2
         */
        float v = tPaint.measureText(text);//文字宽度
        float startX = -v / 2;

        /*
         * 控件高度/2 + 文字高度/2,绘制文字从文字左下角开始,因此"+"
         */
        // float startY = getHeight() / 2 + oRect.height() / 2;
        Paint.FontMetricsInt fm = tPaint.getFontMetricsInt();
        //fm.bottom - fm.top//文字高度
        int startY = -fm.descent + (fm.bottom - fm.top) / 2;
        // 绘制文字
        canvas.drawText(text, startX + CentreX, startY + CentreY, tPaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWith / 2, mHeight / 2);
        if (angle == null) {
            return;
        }
        //final RectF rectF = new RectF(-diameter / 2, -diameter / 2, diameter / 2, diameter / 2);
        final RectF rectF = new RectF();
        if (regionList != null) {
            regionList.clear();
        }
        Float Angle = 0f;
        for (int i = 0; i < angle.length; i++) {
            Angle = i > 0 ? angle[i - 1] + Angle : 0;
            //Log.e("-----startAngle=", Angle + "");
            startAngle = Angle;
            sweepAngle = angle[i];
            angleColor = Color.parseColor(colorstr[i]);
            Float sta = startAngle * multiple / overallMultiple;
            Float swa = sweepAngle * multiple / overallMultiple;
            if (index == i) {
                rectF.set(-diameter / 2 - 10, -diameter / 2 - 10, diameter / 2 + 10, diameter / 2 + 10);
                // index = -1;
            } else {
                rectF.set(-diameter / 2, -diameter / 2, diameter / 2, diameter / 2);
            }
            drawArc(canvas, rectF, sta,
                    swa, angleColor);
        }

        LinearGradient backGradient = new LinearGradient(diameter / 2, -diameter / 2, -diameter / 2, diameter / 2,
                new int[]{ContextCompat.getColor(context, R.color.colorStart),
                        // ContextCompat.getColor(context, R.color.color1),
                        ContextCompat.getColor(context, R.color.colorEnd)},
                null, Shader.TileMode.CLAMP);
        mPaint1.setShader(backGradient);
        // 绘制圆
        // mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, diameter * 3 / 10, mPaint1);
        // 绘制圆
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, diameter * 3 / 10 * 17 / 18, mPaint);
        drawCentreText(canvas, "环形图");
    }

    private final int ACTION_CLICK = 1;//点击
    private final int ACTION_MOVE = 2;//滑动
    private int action;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.e(TAG, "onTouchEvent:  x=" + event.getX() + " y=" + event.getY());
        Float dwX, dwY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dwX = event.getX();
                dwY = event.getY();
                action = ACTION_CLICK;
                break;

            case MotionEvent.ACTION_MOVE:

                // action = ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                switch (action) {
                    case ACTION_CLICK:
                        for (int i = 0; i < regionList.size(); i++) {
                            if (regionList.get(i).contains((int) (event.getX() - (mWith / 2)), (int) (event.getY() - (mHeight / 2)))) {
                                if (index == i) {
                                    index = -1;

                                } else {
                                    index = i;

                                }

                                // invalidate();
                                break;

                            }
                            //Log.e(TAG, "onTouchEvent: oo=" + index);
                        }
                        //  Log.e(TAG, "onTouchEvent: action=" + action);
                        invalidate();
                        break;
                }
                break;

        }
        return true;

    }

    private Boolean isDynamic = false;

    public void setDatas(Float[] datas, Boolean isDynamic) {

        if (angle == null) {
            angle = new Float[datas.length];
        }
        for (int i = 0; i < datas.length; i++) {
            sum = sum + datas[i];
        }
        for (int i = 0; i < datas.length; i++) {
            angle[i] = 360 * (datas[i] / sum);
        }
        this.isDynamic = isDynamic;
        //invalidate();
        if (isDynamic) {
            thread.start();
        }

    }

    public void stapThread() {

        if (thread.isAlive()) {
            isDynamic = false;
        }
    }

    private int time = 5;
    private Thread thread = new Thread(new Runnable() {
        public void run() {

            for (int j = 1; j <= overallMultiple; j++) {
                handler.sendEmptyMessage(0x10);
                if (!isDynamic) {
                    return;
                }
                multiple = j;
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (j > overallMultiple * 7 / 8)
                    time += 1;
            }


        }
    });
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x10)
                invalidate();
        }
    };

}

