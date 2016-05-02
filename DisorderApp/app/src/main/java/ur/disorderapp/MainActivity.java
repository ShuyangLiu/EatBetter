package ur.disorderapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.io.IOException;

import ur.disorderapp.EnumValues.GoalStatus;
import ur.disorderapp.model.Collection;

/*
* The Main / Home page should be like a dashboard-like page that shows the user
* progress and able to navigate to various programs, either to start a new one
* or continue an old one
* */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private Collection sCollection;
    //public Audio mAudio;
    public MediaPlayer mPlayer;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //mAudio.release();
        mPlayer.release();
        mPlayer = null;
    }

    private void play()
    {
        try
        {
            AssetManager assets = getApplicationContext().getAssets();
            AssetFileDescriptor afd = assets.openFd("sound/main.mp3");
            if(mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        }
        catch(IOException ioe) {
            Log.e("TAG", "Failed to play music: " + "sound/main.mp3");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Audio
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mPlayer.start();
                    }
                });

        //Initialize the database collection
        sCollection = Collection.get(this.getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play();
                }
            });
        }


        //Setting up Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //Setting up progress bar
        double progress = sCollection.checkProgress("sugar");

        View headerView = null;
        if (navigationView != null) {
            headerView = navigationView.getHeaderView(0);
        }
        CircularProgressBar sugarProgress = null;
        if (headerView != null) {
            sugarProgress =
                    (CircularProgressBar) headerView.findViewById(R.id.sugar_progress);
        }
        // 2500ms = 2.5s
        int animationDuration = 5000;
        // Default duration = 1500ms
        if (sugarProgress != null) {
            sugarProgress.setProgressWithAnimation((float) progress, animationDuration);
        }

        CircularProgressBar main_sugarProgress =
                (CircularProgressBar) findViewById(R.id.main_progress_sugar);
        if (main_sugarProgress != null) {
            main_sugarProgress.setProgressWithAnimation((float) progress, animationDuration);
        }

        //Buttons
        Button sugarModule = (Button) findViewById(R.id.main_btn_sugar);
        Button exercise = (Button) findViewById(R.id.main_btn_exercise);
        Button sleep = (Button) findViewById(R.id.main_btn_sleep);
        Button other = (Button) findViewById(R.id.main_btn_other);

        //Font
        if (sugarModule != null) {
            sugarModule.setTypeface(Typeface.createFromAsset(getApplicationContext()
                    .getAssets(),"font/Raleway-Light.ttf"));
        }
        if (exercise != null) {
            exercise.setTypeface(Typeface.createFromAsset(getApplicationContext()
                    .getAssets(),"font/Raleway-Light.ttf"));
        }
        if (sleep != null) {
            sleep.setTypeface(Typeface.createFromAsset(getApplicationContext()
                    .getAssets(),"font/Raleway-Light.ttf"));
        }
        if (other != null) {
            other.setTypeface(Typeface.createFromAsset(getApplicationContext()
                    .getAssets(),"font/Raleway-Light.ttf"));
        }

        if (sugarModule != null) {
            sugarModule.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    GoalStatus s = sCollection.checkStatus("sugar");

                    Intent i;
                    //Start a new one
                    if(s==GoalStatus.UNACTIVATED ||
                            s==GoalStatus.SELFMONITORING ||
                            s==GoalStatus.ACTIVATED ||
                            s==GoalStatus.FINISHED)
                    {
                        i = new Intent(getApplicationContext(),
                                PreSelfMonitorActivity.class);
                    }
                    //Feedback and congratulation page
                    // status == PREACTIVATED || PREFINISHED
                    else {
                        i = new Intent(getApplicationContext(),
                                SugarProgramActivity.class);
                    }

                    startActivity(i);
                }
            });
        }

        //If there is no service running right now, start the service
        if (!isMyServiceRunning(DataSendingService.class)){
            Intent i = new Intent(this,DataSendingService.class);
            startService(i);
        }
    }

    //check running service
    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sugar_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    protected void onUserLeaveHint ()
    {
        super.onUserLeaveHint();
        this.finishAffinity();
    }
}
