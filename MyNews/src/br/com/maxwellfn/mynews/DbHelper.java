package br.com.maxwellfn.mynews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static DbHelper mInstance = null;

	private static final String DB_NAME = "favorita";
	public static final String TABLENAME_MYNEWS = "dbMyNews";
	private static final int DB_VERSION = 1;

	public static DbHelper getInstance(Context ctx) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (mInstance == null) {
			mInstance = new DbHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	/**
	 * Constructor should be private to prevent direct instantiation. make call
	 * to static factory method "getInstance()" instead.
	 */
	private DbHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ TABLENAME_MYNEWS
				+ "(_id integer primary key autoincrement,title text, subreddit text, id text, url text, thumbnail text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoNova) {

	}

}
