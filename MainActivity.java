
package com.example.nkaddouralab7;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.TimeAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements Clock.ClockListener, MediaPlayer.OnCompletionListener {
    private Clock clock;
    private static long TIMER_MESC = 100;
    private long timePassed = 0, time = 0;;
    private TextView display;
    private TimeAnimator timeAnimator = new TimeAnimator();
    private SharedPreferences preferences;
    private String face;
    private int interval;
    private boolean timeFormat = false, ticRate;
    private MediaPlayer mediaPlayer;
    private int clipID = R.raw.click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = (Clock) findViewById(R.id.Clock);
        display = (TextView) findViewById(R.id.Time);
        clock.setListener(this);

        timeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                timePassed += deltaTime;

                time += deltaTime;

                if (timePassed >= TIMER_MESC){
                    timePassed = 0;
                    clock.invalidate();
                }

                if (time >= 1000){
                    playSound(R.raw.click);
                    time = 0;
                }
            }
        });

        timeAnimator.start();
    }
    public void playSound(int id){
        if (mediaPlayer != null && id == clipID){
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null){
                mediaPlayer.release();
            }
            clipID = id;
            mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.start();
        }
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        mediaPlayer = null;
    }
    public void onAbout(MenuItem item) {
        Toast.makeText(this,
                "Lab 7, Spring 2020, Naji Kaddoura",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void theTime(Date theDate) {
        if (!timeFormat){
            display.setText(theDate.getHours() + ":" + theDate.getMinutes() + ":" + theDate.getSeconds());
        } else {
            display.setText((theDate.getHours()%12) + ":" + theDate.getMinutes() + ":" + theDate.getSeconds());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onResume(){
        super.onResume();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        face = preferences.getString("CLOCK_FACE", "blank");//clock face
        clock.changeFace(face);

        ticRate = preferences.getBoolean("smoothPref", false);//smooth preference
        changeTic(ticRate);

        boolean timeFormat = preferences.getBoolean("hourFormat", false);//24 hour format
        this.timeFormat = timeFormat;

        interval = Integer.parseInt(preferences.getString("TIMER_INTERVAL", "100"));//tic rate
        TIMER_MESC = interval;
    }
    public void changeTic(boolean ticRate){
        if (ticRate == true) {
            Toast.makeText(this, "Couldnt get it to work", Toast.LENGTH_SHORT).show();
        }
    }
}
