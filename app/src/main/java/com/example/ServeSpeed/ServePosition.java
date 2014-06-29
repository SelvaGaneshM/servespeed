package com.example.ServeSpeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lenovo on 5/24/14.
 */
public class ServePosition extends Activity {

    public ImageView shoe,tennisCourt,shoe2;
    int windowwidth;
    int windowheight;
    float TouchX;
    float TouchY;
    public static float servePositionX;
    public static float servePositionY;
    public static float y_org;
    public static float y_court;
    public static float x_org;
    public static float x_court;

    public static float y_orgDp;
    public static float y_courtDp;
    public static float x_orgDP;
    public static float x_courtDP;

    public static float scaleInDP;
    float screenWidth;
    float screenHeight;
    DisplayMetrics dm;
    Animation toZoomIn;
    float screenwidth1;
    float screenheight1;
    Animation AnimBlink;
    boolean isServePositionSelected;
    public static int HeightPlayerFinal=170;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serve_position);
        shoe = (ImageView)findViewById(R.id.shoefirst);
        shoe2=(ImageView)findViewById(R.id.shoesecond);
        tennisCourt = (ImageView)findViewById(R.id.tennis_court);
        isServePositionSelected=false;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwidth1=dm.widthPixels;
        screenheight1=dm.heightPixels;
        AnimBlink=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        shoe.startAnimation(AnimBlink);
        shoe2.startAnimation(AnimBlink);

        //tennisCourt.setAnimation(toZoomIn);

        SeekBar HeightBar;
        HeightBar=(SeekBar)findViewById(R.id.height_slider);
        HeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int height, boolean b) {

                TextView playerHeight=(TextView)findViewById(R.id.textViewHeightPlayer);
                playerHeight.setText(130+height +"\n" + "cm");
                HeightPlayerFinal=130+height;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        shoe.setOnTouchListener(new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            int eid = event.getAction();
            shoe.clearAnimation();
            shoe2.clearAnimation();
            isServePositionSelected=true;
            switch (eid) {
                case MotionEvent.ACTION_MOVE:

                    FrameLayout.LayoutParams mParams = (FrameLayout.LayoutParams) shoe.getLayoutParams();
                    FrameLayout.LayoutParams mParamsOtherShoe=(FrameLayout.LayoutParams)shoe2.getLayoutParams();
                    mParamsOtherShoe.leftMargin=0;
                    int x_cord = (int) event.getRawX();
                    //int y_cord = (int) event.getRawX();
                    x_org=dm.widthPixels;
                    y_org=dm.heightPixels;
                    x_orgDP=convertPixelsToDp(x_org,getApplicationContext());
                    y_orgDp=convertPixelsToDp(y_org,getApplicationContext());
                    y_courtDp=y_orgDp-100;
                    float length=(float)23.78;
                    float breadth=(float)10.97;
                    x_courtDP=y_courtDp*breadth/length;
                    x_court=convertDpToPixel(x_courtDP,getApplicationContext());
                    y_court=convertDpToPixel(y_courtDp,getApplicationContext());
                    scaleInDP=y_courtDp/length;
                    int convert40=(int)convertDpToPixel(40,getApplicationContext());
                    if(x_cord>x_court + convert40)
                    {
                        x_cord=(int)x_court+ convert40;
                    }
                    if(x_cord<(x_org-x_court)/2 +convert40)
                    {
                        x_cord=(int)(x_org-x_court)/2 + convert40;
                    }
                    if(x_cord>=x_org/2 && x_cord<=x_org/2 + 20)
                    {
                        x_cord=(int)x_org/2+21;
                    }
                    if(x_cord<=x_org/2 && x_cord>=x_org/2-20)
                    {
                        x_cord=(int)x_org/2-21;
                    }


                    mParams.leftMargin = x_cord - (int)screenwidth1/2;



                    shoe.setLayoutParams(mParams);
                    break;
                case MotionEvent.ACTION_UP:
                    int test1[] = new int[2];
                    float x = shoe.getLeft();
                    float y = shoe.getTop();
                    float xdp=convertPixelsToDp(x,getApplicationContext());
                    float ydp=convertPixelsToDp(y,getApplicationContext());
                    servePositionX=xdp+20;
                    servePositionY=50;
                    float screenwidth=dm.widthPixels;
                    float screenheight=dm.heightPixels;
                    screenWidth= convertPixelsToDp(screenwidth,getApplicationContext());
                    screenHeight=convertPixelsToDp(screenheight,getApplicationContext());

                    Toast.makeText(getApplicationContext(), "x-" + servePositionX + "y-" + servePositionY, Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
            return true;
        }
    });



        shoe2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int eid = event.getAction();
                shoe2.clearAnimation();
                shoe.clearAnimation();
                isServePositionSelected=true;
                switch (eid) {
                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams mParams = (FrameLayout.LayoutParams) shoe2.getLayoutParams();
                        FrameLayout.LayoutParams mParamsOtherShoe=(FrameLayout.LayoutParams)shoe.getLayoutParams();
                        mParamsOtherShoe.leftMargin=0;
                        int x_cord = (int) event.getRawX();
                        //int y_cord = (int) event.getRawX();
                        x_org=dm.widthPixels;
                        y_org=dm.heightPixels;
                        x_orgDP=convertPixelsToDp(x_org,getApplicationContext());
                        y_orgDp=convertPixelsToDp(y_org,getApplicationContext());
                        y_courtDp=y_orgDp-100;
                        float length=(float)23.78;
                        float breadth=(float)10.97;
                        x_courtDP=y_courtDp*breadth/length;
                        x_court=convertDpToPixel(x_courtDP,getApplicationContext());
                        y_court=convertDpToPixel(y_courtDp,getApplicationContext());
                        scaleInDP=y_courtDp/length;
                        int convert40=(int)convertDpToPixel(40,getApplicationContext());
                        if(x_cord>x_court + convert40)
                        {
                            x_cord=(int)x_court+ convert40;
                        }
                        if(x_cord<(x_org-x_court)/2 +convert40)
                        {
                            x_cord=(int)(x_org-x_court)/2 + convert40;
                        }
                        if(x_cord>=x_org/2 && x_cord<=x_org/2 + 20)
                        {
                            x_cord=(int)x_org/2+21;
                        }
                        if(x_cord<=x_org/2 && x_cord>=x_org/2-20)
                        {
                            x_cord=(int)x_org/2-21;
                        }


                        mParams.leftMargin = x_cord - (int)screenwidth1/2;

                        //mParams.topMargin = y_cord ;

                        shoe2.setLayoutParams(mParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        int test1[] = new int[2];
                        float x = shoe2.getLeft();
                        float y = shoe2.getTop();
                        float xdp=convertPixelsToDp(x,getApplicationContext());
                       // float ydp=convertPixelsToDp(y_org,getApplicationContext());
                        servePositionX=xdp+20;
                        servePositionY=y_orgDp-50;
                        float screenwidth=dm.widthPixels;
                        float screenheight=dm.heightPixels;
                        screenWidth= convertPixelsToDp(screenwidth,getApplicationContext());
                        screenHeight=convertPixelsToDp(screenheight,getApplicationContext());

                        Toast.makeText(getApplicationContext(), "x-" + servePositionX + "y-" + servePositionY, Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
                return true;
            }
        });
    }
    /*
    private void placeImage(float X, float Y) {
        int touchX = (int) X;
        int touchY = (int) Y;



        //placing at center of touch
        int viewWidth = shoe.getWidth();
        int viewHeight = shoe.getHeight();
        viewWidth = viewWidth / 2;
        viewHeight = viewHeight / 2;

        shoe.layout(touchX -viewWidth , touchY-viewHeight , touchX + viewWidth, touchY+ viewHeight);

        // Toast.makeText(this,"x-" +touchView2.getX()+"y-"+touchView2.getY(),Toast.LENGTH_SHORT).show();


    }
*/
    public void NoteServingPosition(View view)
    {
        if(isServePositionSelected)
        {
            PlayVideo p1= new PlayVideo();
            Intent intent= new Intent(this,PlayVideo.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Select Server Position",Toast.LENGTH_SHORT).show();
        }
    }
    public void ZoomInShoe(View view)
    {
        shoe.setVisibility(View.VISIBLE);
        tennisCourt.startAnimation(toZoomIn);
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


}


