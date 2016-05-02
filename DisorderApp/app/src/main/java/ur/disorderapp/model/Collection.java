package ur.disorderapp.model;

/*
    A singleton class for adding and querying the data in database
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ur.disorderapp.EnumValues.GoalStatus;
import ur.disorderapp.EnumValues.Situation;
import ur.disorderapp.EnumValues.TimePeriod;
import ur.disorderapp.database.DatabaseCursorWrapper;
import ur.disorderapp.database.DatabaseHelper;
import ur.disorderapp.database.Schema;

public class Collection
{

    private static Collection sCollection;
    private final SQLiteDatabase Database;

    public Collection(Context appContext)//Constructor
    {
        Context appContext1 = appContext.getApplicationContext();
        Database = new DatabaseHelper(appContext1).getWritableDatabase();

        //Initialize goal table for demo
        this.addGoal(new Goal(0, GoalStatus.UNACTIVATED, "sugar"));

        //Add an account for demo
        this.addAccount("aaa","aaa");

    }

    private static ContentValues getContentValues_goal(Goal goal) {
        ContentValues values = new ContentValues();

        values.put(Schema.GoalTable.Cols.STATUS, goal.getStatus().toString());
        values.put(Schema.GoalTable.Cols.PROGRESS, goal.getProgress());
        values.put(Schema.GoalTable.Cols.NAME, goal.getName());

        return values;
    }

    private static ContentValues getContentValues_account(String uid, String pw)
    {
        ContentValues values = new ContentValues();

        values.put(Schema.AccountTable.Cols.UID, uid);
        values.put(Schema.AccountTable.Cols.PASSWORD, pw);

        return values;
    }

    private static ContentValues getContentValues_SugarProgram(double sugar_intake,
                                                               double fruit)
    {
        ContentValues values = new ContentValues();

        values.put(Schema.ProgramTable.Cols.SUGARINTAKE,sugar_intake);
        values.put(Schema.ProgramTable.Cols.FRUIT,fruit);

        return values;
    }

    private static ContentValues getContentValues_selfMonitoringData
            (SelfAssessmentData data)
    {
        ContentValues values = new ContentValues();

        values.put(Schema.HabitTable.Cols.AMOUNT, data.getAmount());
        values.put(Schema.HabitTable.Cols.DATE, data.getDate());
        values.put(Schema.HabitTable.Cols.FEELING, data.getFeeling().toString());
        values.put(Schema.HabitTable.Cols.FOOD, data.getFood());
        values.put(Schema.HabitTable.Cols.LOCATION, data.getLocation().toString());
        values.put(Schema.HabitTable.Cols.SITUATION, data.getSituation().toString());
        values.put(Schema.HabitTable.Cols.TIME, data.getTime().toString());
        values.put(Schema.HabitTable.Cols.SENT,data.isSent());

        return values;
    }

    //Adding new data to local database
    public void addGoal(Goal goal)
    {
        ContentValues values = getContentValues_goal(goal);
        Database.insert(Schema.GoalTable.NAME, null, values);
    }
    public void addSelfAssessmentData(SelfAssessmentData data) {
        ContentValues values = getContentValues_selfMonitoringData(data);
        Database.insert(Schema.HabitTable.NAME, null, values);
    }

    public void addAccount(String uid, String pw)
    {
        ContentValues values = getContentValues_account(uid, pw);
        Database.insert(Schema.AccountTable.NAME, null, values);
    }

    public void addSugarProgramData(double sugar_intake, double fruit)
    {
        ContentValues values = getContentValues_SugarProgram(sugar_intake,fruit);
        Database.insert(Schema.ProgramTable.NAME,null,values);
    }

    //Updating goal status / progress using the name of the goal
    public void updateGoal(Goal goal)
    {
        ContentValues values = getContentValues_goal(goal);
        String name = goal.getName();
        Database.update(Schema.GoalTable.NAME, values,
                Schema.GoalTable.Cols.NAME + "=?", new String[]{name});
    }

    public void updateData(SelfAssessmentData data)
    {
        ContentValues values = getContentValues_selfMonitoringData(data);
        String date = data.getDate();
        Database.update(Schema.HabitTable.NAME,values,
                Schema.HabitTable.Cols.DATE + "=?", new String[]{date});
    }

    public void updateStatus(GoalStatus s, String name)
    {
        Goal goal = new Goal(sCollection.checkProgress(name),s,name);
        sCollection.updateGoal(goal);
    }

    public static Collection get(Context c)
    {
        if(sCollection == null) {
            sCollection = new Collection(c);
        }
        return sCollection;
    }

    // Helper Methods for querying data
    private DatabaseCursorWrapper queryGoal(String where, String[] args)
    {
        Cursor cursor = Database.query(Schema.GoalTable.NAME,
                null,where,args,null,
                null,null);

        return new DatabaseCursorWrapper(cursor);
    }
    private DatabaseCursorWrapper querySelfAssessmentData(String where, String[] args)
    {
        Cursor cursor = Database.query(Schema.HabitTable.NAME,
                null,where,args,null,
                null,null);

        return new DatabaseCursorWrapper(cursor);
    }

    private DatabaseCursorWrapper queryAccount(String where, String[] args)
    {
        Cursor cursor = Database.query(Schema.AccountTable.NAME,
                null,where,args,null,
                null,null);
        return new DatabaseCursorWrapper(cursor);
    }

    private DatabaseCursorWrapper queryProgramData(String where, String[] args)
    {
        Cursor cursor = Database.query(Schema.ProgramTable.NAME,
                null,where,args,null,null,null);
        return new DatabaseCursorWrapper(cursor);
    }

    // returns the current status of the goal of given name
    public GoalStatus checkStatus(String name)
    {

        GoalStatus status = null;

        try(DatabaseCursorWrapper wrapper = queryGoal("NAME=?", new String[]{name}))
        {
            wrapper.moveToFirst();

            while (!wrapper.isAfterLast()) {
                Goal goal = wrapper.getGoal();
                status = goal.getStatus();
                wrapper.moveToNext();
            }
        }

        return status;
    }
    // returns the current progress of the goal of given name
    public double checkProgress(String name)
    {

        double progress = 0;

        try(DatabaseCursorWrapper wrapper = queryGoal("NAME=?", new String[]{name}))
        {
            wrapper.moveToFirst();

            while (!wrapper.isAfterLast()) {
                Goal goal = wrapper.getGoal();
                progress = goal.getProgress();
                wrapper.moveToNext();
            }
        }

        return progress;
    }

    public Goal getGoal(String name)
    {
        try(DatabaseCursorWrapper wrapper = queryGoal("NAME=?", new String[]{name}))
        {
            wrapper.moveToFirst();
            return wrapper.getGoal();
        }
    }

    //Return the total number of intake of certain food
    public int getAmountSum(String food)
    {
        int sum = 0;

        try(DatabaseCursorWrapper wrapper = querySelfAssessmentData
                ("FOOD=?", new String[]{food}))
        {
            wrapper.moveToFirst();

            while (!wrapper.isAfterLast()) {
                SelfAssessmentData data = wrapper.getSelfAssessmentData();
                sum += data.getAmount();
                Log.d("TTT",food+"::amount::"+data.getAmount());
                wrapper.moveToNext();
            }
        }

        return sum;
    }

    public double getSum_sugar_intake()
    {
        double sum = 0.0;

        try(DatabaseCursorWrapper wrapper = queryProgramData(null, null))
        {
            wrapper.moveToFirst();

            while (!wrapper.isAfterLast()) {
                sum += wrapper.getProgramAmount_sugar();
                wrapper.moveToNext();
            }
        }

        return sum;
    }
    public double getSum_fruit_intake()
    {
        double sum = 0.0;

        try(DatabaseCursorWrapper wrapper = queryProgramData(null, null))
        {
            wrapper.moveToFirst();

            while (!wrapper.isAfterLast()) {
                sum += wrapper.getProgramAmount_fruit();
                wrapper.moveToNext();
            }
        }

        return sum;
    }

    public double getFirstSugarProgramRow_sugar()
    {
        try(DatabaseCursorWrapper wrapper = queryProgramData(null, null))
        {
            wrapper.moveToFirst();
            return wrapper.getProgramAmount_sugar();
        }
    }

    public double getFirstSugarProgramRow_fruit()
    {
        try(DatabaseCursorWrapper wrapper = queryProgramData(null, null))
        {
            wrapper.moveToFirst();
            return wrapper.getProgramAmount_fruit();
        }
    }

    //return true if the password matches uid
    public boolean login(String uid, String pw)
    {
        try (DatabaseCursorWrapper wrapper = queryAccount("UID=?", new String[]{uid})) {
            wrapper.moveToFirst();
            String password = wrapper.getPassword();
            if(password==null) {
                return false;
            }
            if(password.equals(pw)) {
                return true;
            }
        }
        return false;
    }

    //return a full list of SelfAssessmentData records
    public List<SelfAssessmentData> getSelfAssessmentData()
    {
        List<SelfAssessmentData> list = new ArrayList<>();

        try (DatabaseCursorWrapper wrapper = querySelfAssessmentData(null, null)) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                SelfAssessmentData data = wrapper.getSelfAssessmentData();
                list.add(data);
                wrapper.moveToNext();
            }
        }
        return list;
    }

    //return row number of non-empty data
    public int getTotalRowNum_SelfAssessment()
    {
        int sum = 0;

        try (DatabaseCursorWrapper wrapper = querySelfAssessmentData("NOT FOOD=?",
                new String[]{"NULL"})) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                sum++;
                wrapper.moveToNext();
            }
        }
        return sum;
    }

    public int getTotalRowNum_SugarProgram()
    {
        int sum = 0;

        try (DatabaseCursorWrapper wrapper = queryProgramData(null, null)) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                sum++;
                wrapper.moveToNext();
            }
        }

        return sum;
    }

    //return a list of unsent data
    public List<SelfAssessmentData> getUnsentSelfAssessmentData()
    {
        List<SelfAssessmentData> list = new ArrayList<>();

        try (DatabaseCursorWrapper wrapper = querySelfAssessmentData("SENT=?",
                new String[]{"0"})) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                SelfAssessmentData data = wrapper.getSelfAssessmentData();
                    list.add(data);
                wrapper.moveToNext();
            }
        }
        return list;
    }

    public Situation getMaxAccompany()
    {
        Situation result = Situation.OTHER;

        int family_count = 0;
        int colleague_count = 0;
        int alone_count = 0;

        try (DatabaseCursorWrapper wrapper = querySelfAssessmentData(null, null)) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                SelfAssessmentData data = wrapper.getSelfAssessmentData();
                if (data.getSituation()==Situation.FAMILY) {
                    family_count++;
                } else if (data.getSituation()==Situation.COLLEAGUE){
                    colleague_count++;
                } else if (data.getSituation()==Situation.ALONE){
                    alone_count++;
                }
                wrapper.moveToNext();
            }
        }

        //compare
        if (family_count>colleague_count){
            if (family_count>alone_count){
                result = Situation.FAMILY;
            } else {
               result = Situation.ALONE;
            }
        } else {
            if (colleague_count>alone_count){
                result = Situation.COLLEAGUE;
            } else {
                result = Situation.ALONE;
            }
        }

        return result;


    }

    public TimePeriod getMaxTimePeriod()
    {
        TimePeriod time = TimePeriod.OTHER;

        int morning = 0;
        int noon = 0;
        int night = 0;

        try (DatabaseCursorWrapper wrapper = querySelfAssessmentData(null, null)) {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                SelfAssessmentData data = wrapper.getSelfAssessmentData();
                if (data.getTime()==TimePeriod.MORNING) {
                    morning++;
                } else if (data.getTime()==TimePeriod.NOON){
                    noon++;
                } else if (data.getTime()==TimePeriod.NIGHT){
                    night++;
                }
                wrapper.moveToNext();
            }
        }

        //compare
        if (morning>noon){
            if (morning>night){
                time = TimePeriod.MORNING;
            } else {
                time = TimePeriod.NIGHT;
            }
        } else {
            if (noon>night){
                time = TimePeriod.NOON;
            } else {
                time = TimePeriod.NIGHT;
            }
        }

        return time;
    }


}