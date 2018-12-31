package id.mov.katalogfilm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static String DB_NAME = "dbMovie";
    public static String TABLE_NAME = "favourite";
    public static String FIELD_MOVIE_ID = "movieId";
    public static String FIELD_TITLE = "title";
    public static String FIELD_RELEASE = "releasex";
    public static String FIELD_RATE = "rate";
    public static String FIELD_OVERVIEW = "overview";
    public static String FIELD_COVER = "cover";
    public static String FIELD_POSTERPATH = "posterpath";

    public static String CREATE_TABLE_FAV = "CREATE TABLE " + TABLE_NAME + " ( "
            + FIELD_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FIELD_TITLE + " TEXT, "
            + FIELD_RELEASE + " TEXT, "
            + FIELD_RATE + " DECIMAL, "
            + FIELD_OVERVIEW + " TEXT, "
            + FIELD_COVER + " TEXT, "
            + FIELD_POSTERPATH + " TEXT "
            + " );";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
