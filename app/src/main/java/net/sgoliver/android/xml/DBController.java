package net.sgoliver.android.xml;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * .
 */
public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "DBNoticias.db", null, 1);
        Log.d(LOGCAT, "Created");
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Noticias (idNoticia INTEGER PRIMARY KEY AUTOINCREMENT,title INTEGER, link TEXT,description TEXT, guid TEXT,pubDate TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT,"New Created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Noticias";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertNoticia(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",queryValues.get("title"));
        values.put("link",queryValues.get("link"));
        values.put("description", queryValues.get("description"));
        values.put("guid", queryValues.get("guid"));
        values.put("pubDate", queryValues.get("pubDate"));
        database.insert("Noticias", null, values);
        database.close();
    }
    public ArrayList<HashMap<String, String>> getAllNoticias() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Noticias";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("title", cursor.getString(1));
                map.put("link", cursor.getString(2));
                map.put("description", cursor.getString(3));
                map.put("guid", cursor.getString(4));
                map.put("pubDate", cursor.getString(5));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        // return contact list
        return wordList;
    }

    public boolean existeNoticia (String pDate){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Noticias where pubDate='" + pDate+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }


}
