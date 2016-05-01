package ur.disorderapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String SQL_DELETE_ENTRIES_GoalTable =
            "DROP TABLE IF EXISTS " + Schema.GoalTable.NAME;
    private static final String SQL_DELETE_ENTRIES_HabitTable =
            "DROP TABLE IF EXISTS " + Schema.HabitTable.NAME;
    private static final String SQL_DELETE_ENTRIES_GoalAverage =
            "DROP TABLE IF EXISTS " + Schema.ProgramTable.NAME;
    private static final String SQL_DELETE_ENTRIES_AccountTable =
            "DROP TABLE IF EXISTS " + Schema.AccountTable.NAME;

    public DatabaseHelper(Context context)
    {
        super(context, Schema.DATABASE_NAME, null, Schema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + Schema.GoalTable.NAME
                        + "(_id integer primary key autoincrement, "
                        + Schema.GoalTable.Cols.STATUS + ", "
                        + Schema.GoalTable.Cols.NAME + ", "
                        + Schema.GoalTable.Cols.PROGRESS + ")"
        );

        db.execSQL("create table " + Schema.HabitTable.NAME
                        + "(_id integer primary key autoincrement, "
                        + Schema.HabitTable.Cols.FOOD + ", "
                        + Schema.HabitTable.Cols.AMOUNT + ", "
                        + Schema.HabitTable.Cols.TIME + ", "
                        + Schema.HabitTable.Cols.LOCATION + ", "
                        + Schema.HabitTable.Cols.SITUATION + ", "
                        + Schema.HabitTable.Cols.FEELING + ", "
                        + Schema.HabitTable.Cols.SENT + ", "
                        + Schema.HabitTable.Cols.SIGNAL+", "
                        + Schema.HabitTable.Cols.DATE + ")"
        );

        db.execSQL("create table " + Schema.ProgramTable.NAME
                + "(_id integer primary key autoincrement, "
                + Schema.ProgramTable.Cols.SUGARINTAKE + ", "
                + Schema.ProgramTable.Cols.FRUIT + ")"
        );

        db.execSQL("create table " + Schema.AccountTable.NAME
                        + "(_id integer primary key autoincrement, "
                        + Schema.AccountTable.Cols.UID + ", "
                        + Schema.AccountTable.Cols.PASSWORD + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES_GoalTable);
        db.execSQL(SQL_DELETE_ENTRIES_AccountTable);
        db.execSQL(SQL_DELETE_ENTRIES_GoalAverage);
        db.execSQL(SQL_DELETE_ENTRIES_HabitTable);

        onCreate(db);
    }
}