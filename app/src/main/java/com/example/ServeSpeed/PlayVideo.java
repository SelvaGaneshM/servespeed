package com.example.ServeSpeed;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.Log;
import java.lang.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.lang.Object;

public class PlayVideo extends Activity implements SurfaceHolder.Callback,MediaPlayer.OnSeekCompleteListener{

    public static int RESULT_LOAD_VIDEO=1001;
    public static BitmapDrawable bitmapDrawable;
    public TextView startTimeField,endTimeField;
    private MediaPlayer mediaPlayer;
    private double startTime = 0,finalTime = 0;
    public static int racquetContactTime=0,ballBounceTime=0;
    private double selectedtime2=0;
    private Handler myHandler = new Handler();
    private int forwardTime = 250,backwardTime = 250;
    private SeekBar seekbar;
    private ImageButton playButton,pauseButton,slowMotion,fwdButton;
    private MyVideoView mView;
    private ImageView play_pause;
    private SurfaceHolder holder;
    public static int RacquetTouchOnce=0;
    public Bitmap bitmap;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    Timer timer;
    String TAG = "servespeed";
    Uri selectVideo;
    float screenwidth1;
    float screenheight1;
    DisplayMetrics dm;
    boolean isSlowMotionPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_videoplayer);
        mView = (MyVideoView) findViewById (R.id.videoView);
        holder = mView.getHolder();
        holder.addCallback(this);
        startTimeField =(TextView)findViewById(R.id.textView1);
        endTimeField =(TextView)findViewById(R.id.textView2);
        seekbar = (SeekBar)findViewById(R.id.seekBar1);
        playButton = (ImageButton)findViewById(R.id.play);
        pauseButton = (ImageButton)findViewById(R.id.pause);
        slowMotion = (ImageButton)findViewById(R.id.slowmotion);
        fwdButton = (ImageButton)findViewById(R.id.forward);
        play_pause = (ImageView)findViewById(R.id.play_pause);
        MainActivity m1= new MainActivity();
        selectVideo=m1.ReturnVideoUri();
        pauseButton.setEnabled(false);
        playButton.setEnabled(false);
        slowMotion.setEnabled(false);
        timer = new Timer();
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenwidth1=dm.widthPixels;
        screenheight1=dm.heightPixels;
        isSlowMotionPlaying=false;
        if(!dontShowAgain())
        {
            Intent intent = new Intent(this,HelpVideo.class);
            startActivity(intent);
        }
    }

    private boolean dontShowAgain()
    {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("servespeed",0);
        boolean dontShow = prefs.getBoolean("dontShowAgain",false);
        return dontShow;
    }

    public void RacquetContactTime(View view) throws InterruptedException {
        isSlowMotionPlaying=false;
        if(RacquetTouchOnce==0)
        {
            racquetContactTime = mediaPlayer.getCurrentPosition();
            Toast.makeText(this, "The time is " + racquetContactTime, Toast.LENGTH_SHORT).show();
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();

            CallServeCoordinates();
            RacquetTouchOnce=1;
        }
        else
        {
            ballBounceTime = mediaPlayer.getCurrentPosition();
            if(ballBounceTime>racquetContactTime)
            {

                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                CallBounceCoordinates();
            }
            else
            {
                Toast.makeText(this,"Ball Bounce Time <RacquetContact Time",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void CallBounceCoordinates()
    {
        Intent intent= new Intent(this,ServePosition2.class);
        startActivity(intent);
    }

    public void CallServeCoordinates()
    {
        Intent intent= new Intent(this,ServePosition.class);
        startActivity(intent);
    }

    public void play(View view)
    {
        play_pause.setVisibility(View.GONE);
        isSlowMotionPlaying=false;
        myHandler.removeCallbacks(startSlow);
        synchronized (this) {
            if (mediaPlayer.isPlaying())
                return;
            mediaPlayer.start();
        }
    }

    public void playslowmotion(View view)
    {
        isSlowMotionPlaying=true;
        myHandler.postDelayed(startSlow, 0);

    }

    public void forward(View view)
    {
        play_pause.setVisibility(View.GONE);
        isSlowMotionPlaying=false;
        myHandler.removeCallbacks(startSlow);
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        int currTime= mediaPlayer.getCurrentPosition();
        boolean flag=true;
        int frameTime;
        while(flag)
        {
            mediaPlayer.start();
            SystemClock.sleep(10);
            mediaPlayer.pause();
            int newTime=mediaPlayer.getCurrentPosition();
            if(newTime>currTime)
            {
                frameTime=newTime-currTime;
                flag=false;
            }
        }
    }

    public void play_pause(View view)
    {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    private Runnable startSlow = new Runnable() {
        @Override
        public void run() {
            play_pause.setVisibility(View.GONE);
            mediaPlayer.start();
            SystemClock.sleep(10);
            mediaPlayer.pause();
            boolean boo = myHandler.postDelayed(this, 300);
            Log.d(TAG, "start and bool :" + boo);
        }
    };

    private Runnable stopSlow = new Runnable() {
        @Override
        public void run() {
            mediaPlayer.pause();
            boolean boo = myHandler.postDelayed(this, 200);
            Log.d(TAG, "stop and bool :" + boo);
        }
    };

    private Runnable UpdateWithSeekBarOrUpdateSeekBar = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int value = seekBar.getProgress();
                    startTime = value;
                    mediaPlayer.seekTo((int) startTime);
                }
            });
            myHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view){
        play_pause.setVisibility(View.VISIBLE);
        isSlowMotionPlaying=false;
        myHandler.removeCallbacks(startSlow);
        if (!mediaPlayer.isPlaying())
            return;
        mediaPlayer.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onstart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(RacquetTouchOnce == 1)
            setTitle("BallBounce Time");
        Log.d(TAG,"onresume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onpause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        myHandler.removeCallbacks(UpdateWithSeekBarOrUpdateSeekBar);
        myHandler.removeCallbacks(startSlow);
        mediaPlayer.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surface created");
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setDataSource(getApplicationContext(), selectVideo);
            mediaPlayer.prepare();
        }
        catch(Exception e)
        {
            Log.d(TAG,"exception e :" + e);
        }
        if (mediaPlayer != null)
        {

            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float getScreenPausePosition;
                    getScreenPausePosition=event.getRawY();
                    float heightofScreen=ServePosition.convertPixelsToDp(screenheight1,getApplicationContext());
                    if(ServePosition.convertPixelsToDp(getScreenPausePosition,getApplicationContext())<heightofScreen-50)
                    {
                        myHandler.removeCallbacks(startSlow);
                        if(mediaPlayer.isPlaying() || isSlowMotionPlaying) {
                            play_pause.setVisibility(View.VISIBLE);
                            mediaPlayer.pause();
                            isSlowMotionPlaying=false;
                        }
                        else
                        {
                            play_pause.setVisibility(View.GONE);
                            mediaPlayer.start();
                        }
                    }
                    return false;

                }
            });
            pauseButton.setEnabled(true);
            playButton.setEnabled(true);
            slowMotion.setEnabled(true);
        }
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        seekbar.setMax((int) finalTime);
        endTimeField.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) finalTime))
                )
        );
        startTimeField.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime))
                )
        );
        seekbar.setProgress((int) startTime);
        seekbar.setClickable(true);

        myHandler.postDelayed(UpdateWithSeekBarOrUpdateSeekBar, 100);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfacedestroyed");
        myHandler.removeCallbacks(UpdateWithSeekBarOrUpdateSeekBar);
        myHandler.removeCallbacks(startSlow);
        mediaPlayer.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surface changed");
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.d("servespeed","onseekcomplete");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            Intent intent = new Intent(this,HelpVideo.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}

