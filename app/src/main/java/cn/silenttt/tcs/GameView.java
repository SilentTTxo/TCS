package cn.silenttt.tcs;

/**
 * Created by tt on 15-5-11.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.text.DecimalFormat;
import java.util.Random;

public class GameView extends View implements Runnable {
    /* 声明Paint对象 */
    private Paint mPaint = null;
    private WindowManager wm ;
    private Random rd= new Random();
    private int Hight,Whight,bc,x=0,y=0,fx=1,WL,HL,ex,ey,Wei,Hei;
    private int line,df=100;
    private Bitmap head,headT,background;
    private Canvas cs;
    private GestureDetector gestureDetector;
    private boolean alive=true;
    private int score = 0,skill=0,type;
    private skill SK = new skill();
    public class mL extends GestureDetector.SimpleOnGestureListener {
        public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            //System.out.println(x+" "+y);
            if (Math.abs(x) > Math.abs(y) ) {
                if(x>0) {
                    if (fx != 3) {
                        fx = 1;
                    }
                }
                else {
                    if (fx != 1) {
                        fx = 3;
                    }
                }
            } else{
                if(y>0) {
                    if (fx != 0) {
                        fx = 2;
                    }
                }
                else {
                    if (fx != 2) {
                        fx = 0;
                    }
                }
            }
            headT = Rot((fx-1)*90,head);
            return true;
        }
        public boolean onDoubleTap(MotionEvent e){
            if(alive){
                if(bodynum==0) return true;
                SK.skillNum=bd[1].type;
                for(int i=1;i<bodynum;i++){
                    bd[i].type=bd[i+1].type;
                }
                bodynum--;
                return true;
            }
            else {
                x = 0;
                y = 0;
                alive = true;
                fx = 1;
                bodynum = 0;
                score = 0;
                headT=head;
                return true;
            }
        }
    }                             //TouchEvent
    class body {
        public int x,y,type;
    }
    private body[] bd;
    private int bodynum=0;

    public void CreadEat(){
        ex = rd.nextInt(WL/bc)*bc+line/2;
        ey = rd.nextInt(HL/bc)*bc+line/2;
        type = rd.nextInt(3);
    }
    public GameView(Context context,WindowManager yooooo) {
        super(context);
        wm = yooooo;
        Hei = wm.getDefaultDisplay().getHeight();
        Wei = wm.getDefaultDisplay().getWidth();
        line = Hei/32;
        Hight = Hei-7*line;
        Whight = Wei-line;
        //Hight = wm.getDefaultDisplay().getHeight()-320;
        //Whight = wm.getDefaultDisplay().getWidth()*25/27;80
        WL = Whight-Wei/27-line;
        HL = Hight-line-line;
        bc = line;
        CreadEat();
        bd = new body[1000];
        bd[0] = new body();
        head = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.head);
        head = Bitmap.createScaledBitmap(head, line, line, true);
        background = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.background);
        background = Bitmap.createScaledBitmap(background,Hei,Wei,true);
        headT=head;

        gestureDetector = new GestureDetector(new mL());
        /* 构建对象 */
        mPaint = new Paint();
        /* 开启线程 */
        new Thread(this).start();
        new Thread(SK).start();
    }
    public void DrawHead(int tx,int ty){
        int sx = line,sy = line;
        tx+=sx;ty+=sy;
        cs.drawBitmap(headT,null,new RectF(tx,ty,tx+line,ty+line),mPaint);
    }
    public Bitmap Rot(float angle,Bitmap img){
        Matrix mx = new Matrix();
        if(angle==180){
            mx.postScale(-1, 1);
        }
        else
            mx.postRotate(angle);
        int wight = img.getWidth();
        int height = img.getHeight();
        System.out.println(wight+" "+height);
        return Bitmap.createBitmap(img,0,0,wight,height,mx,true);
    }
    public void Draw(int x,int y,int color){
        int sx = line,sy = line;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        cs.drawRect(sx+x,sy+y,sx+x+line,sy+y+line,mPaint);
    }
    public void GameOver(){
        mPaint.setTextSize(line*2);
        mPaint.setColor(Color.RED);
        alive=false;
        cs.drawText("Score:"+score,Wei/4,Hei/2,mPaint);
    }
    public void DrawEat(int x,int y,int color){
        int sx = line,sy = line;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        cs.drawCircle(x+sx,y+sy,line/2,mPaint);
    }
    public boolean Eat(){
        if(ex==x+line/2&&ey==y+line/2){
            return true;
        }
        else {
            return false;
        }
    }
    public int ColorTran(int a){
        switch (a){
            case 0:return Color.RED;//红色有角三倍速
            case 1:return Color.GREEN;//绿色分数*2
            case 2:return Color.WHITE;//白色减速
        }
        return Color.WHITE;
    }
    public int  V(int score,int skill){
        int ans=50;
        if (score < 2500) {
            ans=300 - score / 10;
        }
        if (score >= 2500) {
            ans=50;
        }
        if(skill==-1) df=100;
        if(skill==0) return ans/3;
        if(skill==1) df=200;
        if(skill==2) return ans*2;
        return ans;
    }
    public String SKILL(int x){
        switch(x){
            case 0: return "红色有角三倍速！";
            case 1: return "分数乘二！";
            case 2: return "减速一半！";
        }
        return "NoSkill";
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /* 设置画布的颜色 */
        canvas.drawBitmap(background,null,new RectF(0,0,Wei,Hei),mPaint);
        cs=canvas;
        /* 设置取消锯齿效果 */
        mPaint.setAntiAlias(true);

        /* 设置裁剪区域 */

        /* 线锁定画布 */
        canvas.save();
        /* 旋转画布 */
        //canvas.rotate(45.0f);

        /* 设置颜色及绘制矩形 */
        mPaint.setTextSize(line);
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(), mPaint);
        cs.drawText("Score:"+score+"  V="+new DecimalFormat(".00").format(1000.0/V(score,SK.skillNum))+"  skill="+SKILL(SK.skillNum),line,Hight+line,mPaint);

        /* 解除画布的锁定 */
        canvas.restore();
        if(Eat()){
            if(bodynum==0){
                bd[0]=new body();
                bd[0].x=x;
                bd[0].y=y;
            }
            score+=df;
            bodynum++;
            bd[bodynum]=new body();
            bd[bodynum].x=ex-line/2;bd[bodynum].y=ey-line/2;bd[bodynum].type=type;
            CreadEat();
        }
        for(int i=bodynum;i>=0;i--){
            if(i==0){
                switch (fx){
                    case 0:y-=bc;break;
                    case 1:x+=bc;break;
                    case 2:y+=bc;break;
                    case 3:x-=bc;break;
                }
                bd[i].x=x;bd[i].y=y;
                DrawHead(x,y);
                //Draw(x,y,Color.YELLOW);
            }
            else{
                if(bd[i].x!=bd[i-1].x||bd[i].y!=bd[i-1].y) {
                    bd[i].x = bd[i - 1].x;
                    bd[i].y = bd[i - 1].y;
                    DrawEat(bd[i].x+line/2, bd[i].y+line/2, ColorTran(bd[i].type));
                }
            }
        }
        if(x>WL||x<0||y>HL||y<0){
            GameOver();
        }
        DrawEat(ex,ey,ColorTran(type));
        /* 设置颜色及绘制另一个矩形 */
        /*
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(new Rect(line,line,Whight,Hight), mPaint);
        */
    }

    // 触笔事件
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    // 按键按下事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    // 按键弹起事件
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return true;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(V(score,SK.skillNum));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // 使用postInvalidate可以直接在线程中更新界面
            if(alive) postInvalidate();
        }
    }
}
