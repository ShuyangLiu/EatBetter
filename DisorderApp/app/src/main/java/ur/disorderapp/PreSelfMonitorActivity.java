package ur.disorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ur.disorderapp.EnumValues.GoalStatus;
import ur.disorderapp.model.Collection;
import ur.disorderapp.model.Goal;

public class PreSelfMonitorActivity extends AppCompatActivity
{

    Collection sCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_self_monitor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        sCollection = Collection.get(getApplicationContext());

        TextView instruction = (TextView) findViewById(R.id.pre_self_monitor_text);
        Button btn = (Button) findViewById(R.id.pre_self_monitor_btn);

        //Cancel button navigates back to main activity
        Button cancel = (Button) findViewById(R.id.pre_self_monitor_btn_2);
        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        GoalStatus status = sCollection.checkStatus("sugar");

        if (status == GoalStatus.UNACTIVATED || status == GoalStatus.FINISHED){
            String msg;
            if(status == GoalStatus.UNACTIVATED) {
                msg = "It seems this is the first time you start this program.\n" +
                        "You need to finish the self-assessment before you can start\n" +
                        "(And some other instructions about self-assessment here)";
            } else {
                msg = "It seems you have done this program before\n" +
                        "you can start it again or go back to main menu";
            }

            if (instruction != null) {
                instruction.setText(msg);
            }

            if (btn != null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start_self_monitor();
                    }
                });
            }
        } else if (status == GoalStatus.SELFMONITORING){
            String msg = "It seems you have already started the self-monitoring\n"+
                    "You need to continue the assessments until it is finished\n" +
                    "Click on the ok button to continue";
            if (instruction != null) {
                instruction.setText(msg);
            }

            if (btn != null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        continue_self_monitor();
                    }
                });
            }
        } else if (status == GoalStatus.ACTIVATED) {
            String msg = "It seems you have already started the program\n"+
                    "Click on the ok button to continue";
            if (instruction != null) {
                instruction.setText(msg);
            }

            if (btn != null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        continue_program();
                    }
                });
            }
        }
    }

    public void start_self_monitor()
    {
        //Update Goal status and progress
        Goal goal = new Goal(10,GoalStatus.SELFMONITORING,"sugar");
        sCollection.updateGoal(goal);
        //Go to self assessment activity
        Intent i = new Intent(this,SelfAssessmentActivity.class);
        startActivity(i);
    }

    public void continue_self_monitor()
    {
        //Go to self assessment activity
        Intent i = new Intent(this,SelfAssessmentActivity.class);
        startActivity(i);
    }

    public void continue_program()
    {
        //Go to self assessment activity
        Intent i = new Intent(this,SugarProgramActivity.class);
        startActivity(i);
    }

    @Override
    protected void onUserLeaveHint ()
    {
        super.onUserLeaveHint();
        this.finishAffinity();
    }

}
