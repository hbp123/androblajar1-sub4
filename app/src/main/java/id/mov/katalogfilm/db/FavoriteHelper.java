package id.mov.katalogfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import id.mov.katalogfilm.model.Movie;

import static android.provider.BaseColumns._ID;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.COVER;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.MOVIE_ID;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.OVERVIEW;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.POSTERPATH;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.RATE;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.RELEASE;
import static id.mov.katalogfilm.db.DatabaseContract.FavColumn.TITLE;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public Cursor queryAllData() {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY "
                + DatabaseHelper.FIELD_MOVIE_ID + " DESC ", null);
    }

    public ArrayList<Movie> query() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, MOVIE_ID + " DESC ", null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount()>0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(RATE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(COVER)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTERPATH)));

                arrayList.add(movie);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Movie> getAllData() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                movie.setPopularity(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_RATE))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_RELEASE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_OVERVIEW)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_COVER)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_POSTERPATH)));
                arrayList.add(movie);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Movie> getBanner() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_COVER)));
                arrayList.add(movie);
                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.FIELD_MOVIE_ID, movie.getId());
        initialValues.put(DatabaseHelper.FIELD_TITLE, movie.getTitle());
        initialValues.put(DatabaseHelper.FIELD_RATE, movie.getPopularity());
        initialValues.put(DatabaseHelper.FIELD_RELEASE, movie.getReleaseDate());
        initialValues.put(DatabaseHelper.FIELD_OVERVIEW, movie.getOverview());
        initialValues.put(DatabaseHelper.FIELD_COVER, movie.getPosterPath());
        initialValues.put(DatabaseHelper.FIELD_POSTERPATH, String.valueOf(movie.getBackdropPath()));
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void checkdata () {
        Cursor checkdata = database.rawQuery("SELECT * FROM " + DATABASE_TABLE , null);
        if (checkdata.getCount() > 0) {
            Log.e("Check" , "Data are exist");
        }
        else {
            Log.e("Check", "Data doesn't exist");
        }
    }


    public void insertTransaction(Movie movie) {
        String sql = "INSERT INTO " + DATABASE_TABLE + " ( "
                + DatabaseHelper.FIELD_MOVIE_ID + ", "
                + DatabaseHelper.FIELD_TITLE + ", "
                + DatabaseHelper.FIELD_RATE + ", "
                + DatabaseHelper.FIELD_RELEASE + ", "
                + DatabaseHelper.FIELD_OVERVIEW + ", "
                + DatabaseHelper.FIELD_COVER + ", "
                + DatabaseHelper.FIELD_POSTERPATH + ") VALUES (?, ?, ?, ?, ?, ?, ?);";
        database.beginTransaction();

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, String.valueOf(movie.getId()));
        statement.bindString(2, movie.getTitle());
        statement.bindString(3, String.valueOf(movie.getPopularity()));
        statement.bindString(4, movie.getReleaseDate());
        statement.bindString(5, movie.getOverview());
        statement.bindString(6, movie.getPosterPath());
        statement.bindString(7, String.valueOf(movie.getBackdropPath()));

        statement.execute();
        statement.clearBindings();

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void delete(int id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_MOVIE_ID + " = ' " + id + " '", null);
    }

    public Boolean checkid (String id) {
        Cursor check = database.rawQuery("SELECT " + DatabaseHelper.FIELD_MOVIE_ID + " FROM " + DATABASE_TABLE + " WHERE " + DatabaseHelper.FIELD_MOVIE_ID + " = " + id, null);
        if (check.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor queryById (String id) {
        return database.rawQuery("SELECT " + DatabaseHelper.FIELD_MOVIE_ID + " FROM " + DATABASE_TABLE + " WHERE " + id + " = " + id, null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null, MOVIE_ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, MOVIE_ID + " = ? ", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ? ", new String[]{id});
    }

}
