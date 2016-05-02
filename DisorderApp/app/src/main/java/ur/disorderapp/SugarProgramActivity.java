package ur.disorderapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import ur.disorderapp.EnumValues.GoalStatus;
import ur.disorderapp.EnumValues.Situation;
import ur.disorderapp.EnumValues.TimePeriod;
import ur.disorderapp.model.Collection;
import ur.disorderapp.model.Goal;

public class SugarProgramActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public static Collection sCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sCollection = Collection.get(this.getApplicationContext());

        setContentView(R.layout.activity_sugar_program);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Play some audio instructions",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        Button btn_continue = (Button) findViewById(R.id.sugar_program_btn);

        assert btn_continue != null;
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Goal g = sCollection.getGoal("sugar");
                if (g.getStatus() == GoalStatus.PREACTIVATED){
                    GoalStatus s = GoalStatus.ACTIVATED;
                    g.setStatus(s);
                    sCollection.updateGoal(g);
                } else if (g.getStatus() == GoalStatus.PREFINISHED){
                    GoalStatus s = GoalStatus.FINISHED;
                    g.setStatus(s);
                    sCollection.updateGoal(g);
                }

                Intent i = new Intent(getApplicationContext(),
                        PreSelfMonitorActivity.class);

                startActivity(i);
            }
        });

        //Data
        Situation ac = sCollection.getMaxAccompany();
        TimePeriod tm = sCollection.getMaxTimePeriod();
        double rows = (double) sCollection.getTotalRowNum_SelfAssessment();
        int fruit_intake = (int) ((sCollection.getAmountSum("Fruit"))/rows);
        int donut_intake = (int) ((sCollection.getAmountSum("Donut"))/rows);
        int soda_intake = (int) ((sCollection.getAmountSum("Soda"))/rows);
        int candy_bar_intake = (int) ((sCollection.getAmountSum("Candy_Bar"))/rows);

        int ResourceId_num [] = {
                R.drawable.zero,
                R.drawable.one,
                R.drawable.two,
                R.drawable.three,
                R.drawable.four
        };

        int fruit_id = ResourceId_num[fruit_intake];
        int donut_id = ResourceId_num[donut_intake];
        int soda_id = ResourceId_num[soda_intake];
        int candy_id = ResourceId_num[candy_bar_intake];

        int ac_id;
        int tm_id;

        if (ac==Situation.FAMILY){
            ac_id = R.drawable.fam;
        } else if (ac==Situation.COLLEAGUE){
            ac_id = R.drawable.colleague;
        } else {
            ac_id = R.drawable.alone;
        }

        if (tm==TimePeriod.MORNING){
            tm_id = R.drawable.morning;
        } else if (tm==TimePeriod.NOON){
            tm_id = R.drawable.afternoon;
        } else {
            tm_id = R.drawable.night;
        }

        try {

            ((ImageView) findViewById(R.id.sugar_program_accompany_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            ac_id, 100, 100));

            ((ImageView) findViewById(R.id.sugar_program_candy_bar_average_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            candy_id, 100, 100));

            ((ImageView) findViewById(R.id.sugar_program_fruit_average_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            fruit_id, 100, 100));

            ((ImageView) findViewById(R.id.sugar_program_donut_average_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            donut_id, 100, 100));

            ((ImageView) findViewById(R.id.sugar_program_soda_average_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            soda_id, 100, 100));

            ((ImageView) findViewById(R.id.sugar_program_time_period_img))
                    .setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                            tm_id, 100, 100));

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sugar_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onUserLeaveHint ()
    {
        super.onUserLeaveHint();
        this.finishAffinity();
    }
}
