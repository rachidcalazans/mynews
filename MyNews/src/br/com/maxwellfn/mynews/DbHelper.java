package br.com.maxwellfn.mynews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME_FAVORITA = "favorita";
	public static final String DB_MY_NEWS = "dbMyNews";

	public DbHelper(Context context) {

		super(context, DB_MY_NEWS, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ TABLE_NAME_FAVORITA
				+ "(_id integer primary key autoincrement,title text, subreddit text, id text, url text, thumbnail text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {

	}

}
