package com.example.ServeSpeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.os.CountDownTimer;
import java.net.URI;

/**
 * Created by lenovo on 6/8/14.
 */
public class CalculateSpeedClass extends Activity{

    TextView tv;
    ImageView image;
    Uri selectVideo;
    Button shareButton;
    Button cont;
    int speeddisplay;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speeed_display);
        tv = (TextView)findViewById(R.id.textView);
        image = (ImageView)findViewById(R.id.snapshot);
        shareButton = (Button)findViewById(R.id.share);
        cont = (Button)findViewById(R.id.cont);
        MainActivity m1= new MainActivity();
        selectVideo=m1.ReturnVideoUri();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try
        {

            retriever.setDataSource(getApplicationContext(),selectVideo);
            Bitmap bitmap = retriever.getFrameAtTime(PlayVideo.racquetContactTime*1000,MediaMetadataRetriever.OPTION_CLOSEST);
            if(bitmap==null)
            {
                bitmap = retriever.getFrameAtTime(-1);
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File f = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "snapshot.jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (Exception e)
        {
            Log.d("servespeed", "exception :" + e);
        }
        Bitmap bm = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "snapshot.jpg");
        image.setImageBitmap(bm);
        speeddisplay=CalcSpeed();
        SystemClock.sleep(500);

        MyCount counter= new MyCount(speeddisplay*20,20);
        counter.start();
    }

    public class MyCount extends CountDownTimer
    {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv.setText("Speed " + speeddisplay  + "km/hr");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv.setText("Speed " + (speeddisplay*20- millisUntilFinished )/ 20 + "km/hr");
        }
    }
    private int CalcSpeed()
    {
        float finalshoeX,finalshoeY,finalballX,finalballY,time1,time2;

        time1= PlayVideo.racquetContactTime;
        time2= PlayVideo.ballBounceTime;
        finalshoeX=ServePosition.servePositionX;
        finalshoeY=ServePosition.servePositionY;
        finalballX= ServePosition2.ballX;
        finalballY=ServePosition2.ballY;
        float scale=ServePosition.scaleInDP;
        float distanceX;
        float distanceY;

        if(finalballX>finalshoeX)
            distanceX=finalballX-finalballX;
        else
            distanceX = finalshoeX-finalballX;

        if(finalballY>finalshoeY)
            distanceY=finalballY-finalshoeY;
        else
            distanceY=finalshoeY-finalballY;

        float distanceXinM=distanceX/scale;
        float distanceYinM=distanceY/scale;

        float height=(float)2.60;

        float time=time2-time1;

        float finaldistanceinM= (float)Math.sqrt(distanceXinM * distanceXinM + distanceYinM * distanceYinM);
        float totalDistance= (float)Math.sqrt(finaldistanceinM*finaldistanceinM + height*height);
        float totalDistanceint=(int)totalDistance;
        float speed= totalDistance*3600/time;
        speed= speed* (float)1.2;
        int speedint=(int)speed;
        return speedint;
    }

    public void share(View view)
    {
        SystemClock.sleep(50);
        String mPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "share.png";
        Bitmap bitmap;
        View v1 = image.getRootView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        OutputStream fout = null;
        File imageFile = new File(mPath);
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendShareIntent(mPath);
    }

    private void sendShareIntent(String path)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        startActivity(shareIntent);
    }

    public void continueGame(View view)
    {
        PlayVideo.RacquetTouchOnce=0;
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
