package ur.disorderapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import ur.disorderapp.EnumValues.Feeling;
import ur.disorderapp.EnumValues.Location;
import ur.disorderapp.EnumValues.Situation;
import ur.disorderapp.EnumValues.TimePeriod;
import ur.disorderapp.model.Collection;
import ur.disorderapp.model.SelfAssessmentData;

public class Timer_Notification_Service extends IntentService {

    public Timer_Notification_Service() {
        super("Timer_Notification_Service");
    }
    public static Collection sCollection;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "timer starting", Toast.LENGTH_SHORT).show();
        sCollection = Collection.get(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                while(true) {
                    Thread.sleep(30000);//Every 30 seconds
                    // create a handler to post messages to the main thread
                    Handler mHandler = new Handler(getMainLooper());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            NotificationCompat.Builder mBuilder =
                                    (NotificationCompat.Builder)
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.drawable.ic_menu_send)
                                                    .setContentTitle("Reminder")
                                                    .setContentText("Don't forget to report every time you eat something!");

                            // Creates an explicit intent for an Activity in the app
                            Intent resultIntent = new Intent(getBaseContext(), MainActivity.class);

                            // The stack builder object will contain an artificial back stack for the
                            // started Activity.
                            // This ensures that navigating backward from the Activity leads out of
                            // your application to the Home screen.
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

                            // Adds the back stack for the Intent (but not the Intent itself)
                            stackBuilder.addParentStack(MainActivity.class);

                            // Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            // mId allows you to update the notification later on.
                            mNotificationManager.notify(1, mBuilder.build());

                            //Toast.makeText(getApplicationContext(), "Timer Stopped", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Thread.sleep(30000);//wait 30 seconds for response

                    //sent empty data
                    //Note: it will never get to here if the user responded to the notification and report new data
                    //Because every time the user sends a set of data, this service will be restarted
                    //which means it will re-entering the loop

                    SelfAssessmentData data =
                            new SelfAssessmentData("NULL",0,
                                    TimePeriod.NULL, Location.NULL, Situation.NULL,
                                    Feeling.NULL,1);

                    sCollection.addSelfAssessmentData(data);
                }

            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
        }
    }


}
