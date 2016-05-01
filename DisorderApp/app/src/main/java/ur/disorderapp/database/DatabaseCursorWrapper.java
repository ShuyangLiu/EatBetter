package ur.disorderapp.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import ur.disorderapp.EnumValues.Feeling;
import ur.disorderapp.EnumValues.Location;
import ur.disorderapp.EnumValues.Situation;
import ur.disorderapp.EnumValues.TimePeriod;
import ur.disorderapp.model.Goal;
import ur.disorderapp.EnumValues.GoalStatus;
import ur.disorderapp.model.SelfAssessmentData;

public class DatabaseCursorWrapper extends CursorWrapper
{
    public DatabaseCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Goal getGoal()
    {
        double progress = getDouble(getColumnIndex(Schema.GoalTable.Cols.PROGRESS));
        GoalStatus status = GoalStatus.valueOf(
                getString(
                        getColumnIndex(
                                Schema.GoalTable.Cols.STATUS)));

        String name = getString(getColumnIndex(Schema.GoalTable.Cols.NAME));

        return new Goal(progress, status, name);
    }


    public double getProgramAmount_sugar()
    {
        return getDouble(getColumnIndex(Schema.ProgramTable.Cols.SUGARINTAKE));
    }
    public double getProgramAmount_fruit()
    {
        return getDouble(getColumnIndex(Schema.ProgramTable.Cols.FRUIT));
    }



    public String getPassword()
    {
        try {
            return getString(getColumnIndex(Schema.AccountTable.Cols.PASSWORD));
        } catch (Exception e) {
            return null;
        }

    }

    public SelfAssessmentData getSelfAssessmentData()
    {
        String food = getString(getColumnIndex(Schema.HabitTable.Cols.FOOD));

        int amount = getInt(getColumnIndex(Schema.HabitTable.Cols.AMOUNT));

        TimePeriod time = TimePeriod.valueOf(
                getString(
                        getColumnIndex(
                                Schema.HabitTable.Cols.TIME)));

        Location location = Location.valueOf(
                getString(
                        getColumnIndex(
                                Schema.HabitTable.Cols.LOCATION)));

        Situation situation = Situation.valueOf(
                getString(
                        getColumnIndex(
                                Schema.HabitTable.Cols.SITUATION)));

        Feeling feeling = Feeling.valueOf(
                getString(
                        getColumnIndex(
                                Schema.HabitTable.Cols.FEELING)));

        String isSent = getString(getColumnIndex(Schema.HabitTable.Cols.SENT));

        int signal = getInt(getColumnIndex(Schema.HabitTable.Cols.SIGNAL));

        String date = getString(getColumnIndex(Schema.HabitTable.Cols.DATE));

        SelfAssessmentData data = new SelfAssessmentData(food,amount,time,
                location,situation,feeling,signal);
        data.setSent(isSent);
        data.setDate(date);

        return data;
    }

}
